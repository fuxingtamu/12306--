const XLSX = require('xlsx');
const mysql = require('mysql2/promise');

// 读取Excel文件
const workbook = XLSX.readFile('D:/xmy/12306旅客管理/Parse12306/output/全国高速列车时刻表_20160310.xlsx');

// 辅助函数
function excelTimeToStr(num) {
    if (num === null || num === undefined || num === '----') return null;
    const hours = Math.floor(num * 24);
    const minutes = Math.floor((num * 24 * 60) % 60);
    const seconds = Math.floor((num * 24 * 60 * 60) % 60);
    return `${String(hours).padStart(2, '0')}:${String(minutes).padStart(2, '0')}:${String(seconds).padStart(2, '0')}`;
}

function calcTotalMinutes(start, end) {
    if (start === null || end === null) return 0;
    return Math.round((end - start) * 24 * 60);
}

function getTrainTypeCode(type) {
    switch (type) {
        case '高铁': return 1;
        case '动车': return 2;
        case '城际': return 3;
        default: return 2;
    }
}

// ============= 解析数据 =============
console.log('解析车站数据...');
const stationSheet = workbook.Sheets['车站'];
const stationData = XLSX.utils.sheet_to_json(stationSheet, { header: 1 });
const stationNameToCode = new Map();
for (let i = 1; i < stationData.length; i++) {
    const row = stationData[i];
    if (row && row.length >= 3) {
        stationNameToCode.set(row[2], row[1]);
    }
}
console.log(`车站: ${stationNameToCode.size}`);

console.log('解析车次数据...');
const trainSheet = workbook.Sheets['车次'];
const trainData = XLSX.utils.sheet_to_json(trainSheet, { header: 1 });
const trainMap = new Map();
for (let i = 1; i < trainData.length; i++) {
    const row = trainData[i];
    if (!row || row.length < 7 || !row[0]) continue;
    const [trainCode, start, end, startTime, endTime, type] = row;
    if (!trainMap.has(trainCode)) {
        trainMap.set(trainCode, {
            code: trainCode,
            startName: start,
            endName: end,
            startTime: excelTimeToStr(startTime),
            endTime: excelTimeToStr(endTime),
            totalMinutes: calcTotalMinutes(startTime, endTime),
            trainType: getTrainTypeCode(type)
        });
    }
}
console.log(`车次: ${trainMap.size}`);

console.log('解析时刻表...');
const scheduleSheet = workbook.Sheets['时刻表'];
const scheduleData = XLSX.utils.sheet_to_json(scheduleSheet, { header: 1 });

// 按 (车次, 站序) 分组，每车次只取第一条记录
const scheduleMap = new Map();
let currentTrainCode = null;
let currentStart = null;
let currentEnd = null;

for (let i = 1; i < scheduleData.length; i++) {
    const row = scheduleData[i];
    if (!row || row.length < 9) continue;

    const [trainCode, start, end, , , , , stopOrder, stationName, arriveTime, departTime, stopDuration, isOpen] = row;

    if (trainCode) {
        currentTrainCode = trainCode;
        currentStart = start;
        currentEnd = end;
    }

    if (!currentTrainCode || !stationName || stopOrder === undefined) continue;

    const key = `${currentTrainCode}-${stopOrder}`;
    if (!scheduleMap.has(key)) {
        scheduleMap.set(key, {
            trainCode: currentTrainCode,
            stationName: stationName,
            stopOrder: stopOrder,
            arriveTime: excelTimeToStr(arriveTime),
            departTime: excelTimeToStr(departTime),
            stopDuration: stopDuration === '----' ? 2 : (parseInt(stopDuration) || 2),
            isStart: stationName === currentStart ? 1 : 0,
            isEnd: stationName === currentEnd ? 1 : 0
        });
    }
}

// 转换为数组
const scheduleList = Array.from(scheduleMap.values());
console.log(`时刻表: ${scheduleList.length}`);

// ============= 数据库导入 =============
async function importData() {
    console.log('\n连接数据库...');
    const pool = mysql.createPool({
        host: 'localhost',
        user: 'root',
        password: '123456',
        database: 'railway_system',
        waitForConnections: true,
        connectionLimit: 10
    });

    try {
        console.log('\n1. 获取车站映射...');
        const [stations] = await pool.execute('SELECT id, station_name, station_code FROM stations');
        const stationIdByName = new Map();
        for (const s of stations) {
            stationIdByName.set(s.station_name, s.id);
        }
        console.log(`   车站ID: ${stationIdByName.size}`);

        console.log('\n2. 清空数据...');
        await pool.execute('SET FOREIGN_KEY_CHECKS = 0');
        await pool.execute('TRUNCATE TABLE train_stops');
        await pool.execute('TRUNCATE TABLE trains');
        await pool.execute('SET FOREIGN_KEY_CHECKS = 1');
        console.log('   完成');

        console.log('\n3. 导入车次...');
        let trainCount = 0;
        for (const [code, t] of trainMap) {
            const startId = stationIdByName.get(t.startName);
            const endId = stationIdByName.get(t.endName);
            if (!startId || !endId) continue;
            await pool.execute(
                `INSERT INTO trains (train_code, train_type, start_station_id, end_station_id,
                 start_station_name, end_station_name, start_time, end_time, total_time, status)
                 VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 1)`,
                [t.code, t.trainType, startId, endId, t.startName, t.endName, t.startTime, t.endTime, t.totalMinutes]
            );
            trainCount++;
        }
        console.log(`   导入 ${trainCount} 个车次`);

        console.log('\n4. 获取车次ID...');
        const [trains] = await pool.execute('SELECT id, train_code FROM trains');
        const trainIdByCode = new Map();
        for (const t of trains) {
            trainIdByCode.set(t.train_code, t.id);
        }
        console.log(`   ${trainIdByCode.size}`);

        console.log('\n5. 导入时刻表...');
        let stopCount = 0;
        for (const s of scheduleList) {
            const trainId = trainIdByCode.get(s.trainCode);
            const stationId = stationIdByName.get(s.stationName);
            if (!trainId || !stationId) continue;
            try {
                await pool.execute(
                    `INSERT INTO train_stops
                     (train_id, train_code, station_id, station_name, stop_order,
                      arrive_time, depart_time, stop_duration, is_start, is_end, status)
                     VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 1)`,
                    [trainId, s.trainCode, stationId, s.stationName, s.stopOrder,
                     s.arriveTime, s.departTime, s.stopDuration, s.isStart, s.isEnd]
                );
                stopCount++;
            } catch (e) {
                // 忽略重复错误
            }
        }
        console.log(`   导入 ${stopCount} 条`);

        console.log('\n6. 验证...');
        const [[trainTotal]] = await pool.execute('SELECT COUNT(*) as c FROM trains');
        const [[stopTotal]] = await pool.execute('SELECT COUNT(*) as c FROM train_stops');
        console.log(`   trains: ${trainTotal.c}`);
        console.log(`   train_stops: ${stopTotal.c}`);

        console.log('\n========== 完成 ==========');
    } finally {
        await pool.end();
    }
}

importData().catch(console.error);
