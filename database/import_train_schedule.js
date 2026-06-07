const XLSX = require('xlsx');
const fs = require('fs');

// 读取Excel文件
const workbook = XLSX.readFile('D:/xmy/12306旅客管理/Parse12306/output/全国高速列车时刻表_20160310.xlsx');

// 辅助函数：将Excel时间分数转换为HH:MM:SS格式
function excelTimeToStr(num) {
    if (num === null || num === undefined || num === '----') return null;
    const hours = Math.floor(num * 24);
    const minutes = Math.floor((num * 24 * 60) % 60);
    const seconds = Math.floor((num * 24 * 60 * 60) % 60);
    return `${String(hours).padStart(2, '0')}:${String(minutes).padStart(2, '0')}:${String(seconds).padStart(2, '0')}`;
}

// 辅助函数：计算总时间（分钟）
function calcTotalMinutes(start, end) {
    if (start === null || end === null) return 0;
    const startMinutes = start * 24 * 60;
    const endMinutes = end * 24 * 60;
    return Math.round(endMinutes - startMinutes);
}

// 辅助函数：获取列车类型代码
function getTrainType(type) {
    switch (type) {
        case '高铁': return 'G';
        case '动车': return 'D';
        case '城际': return 'C';
        default: return 'D';
    }
}

// 辅助函数：脱敏证件号
function maskIdCard(idCard) {
    if (!idCard || idCard.length < 8) return idCard;
    return idCard.substring(0, 4) + '****' + idCard.substring(idCard.length - 4);
}

// ============= 解析车站数据 =============
console.log('正在解析车站数据...');
const stationSheet = workbook.Sheets['车站'];
const stationData = XLSX.utils.sheet_to_json(stationSheet, { header: 1 });

const stationMap = new Map(); // 电报码 -> { id, name, code }
const stationInserts = [];

for (let i = 1; i < stationData.length; i++) {
    const row = stationData[i];
    if (!row || row.length < 3) continue;

    const [seq, code, name, pinyin, letter, pinyinCode, longitude, latitude] = row;
    if (!code || !name) continue;

    stationInserts.push({
        code: code,
        name: name,
        pinyin: pinyin || '',
        letter: letter || '',
        pinyinCode: pinyinCode || '',
        longitude: longitude || 0,
        latitude: latitude || 0
    });
}

console.log(`解析到 ${stationInserts.length} 个车站`);

// ============= 解析车次数据 =============
console.log('正在解析车次数据...');
const trainSheet = workbook.Sheets['车次'];
const trainData = XLSX.utils.sheet_to_json(trainSheet, { header: 1 });

const trainMap = new Map(); // 车次 -> { start, end, startTime, endTime, type }
const trainInserts = [];

for (let i = 1; i < trainData.length; i++) {
    const row = trainData[i];
    if (!row || row.length < 7) continue;

    const [trainCode, start, end, startTime, endTime, type, service] = row;
    if (!trainCode) continue;

    const trainType = getTrainType(type);
    const startStr = excelTimeToStr(startTime);
    const endStr = excelTimeToStr(endTime);
    const totalMinutes = calcTotalMinutes(startTime, endTime);

    trainInserts.push({
        code: trainCode,
        start: start,
        end: end,
        startTime: startStr,
        endTime: endStr,
        type: trainType,
        typeName: type,
        service: service || 2,
        totalMinutes: totalMinutes
    });

    trainMap.set(trainCode, { start, end, startTime, endTime, type });
}

console.log(`解析到 ${trainInserts.length} 个车次`);

// ============= 解析时刻表数据 =============
console.log('正在解析时刻表数据...');
const scheduleSheet = workbook.Sheets['时刻表'];
const scheduleData = XLSX.utils.sheet_to_json(scheduleSheet, { header: 1 });

const scheduleInserts = [];
let currentTrainCode = null;
let currentStart = null;
let currentEnd = null;
let currentStartTime = null;
let currentEndTime = null;
let currentType = null;

for (let i = 1; i < scheduleData.length; i++) {
    const row = scheduleData[i];
    if (!row || row.length < 9) continue;

    const [trainCode, start, end, startTime, endTime, type, service, stopOrder, stationName, arriveTime, departTime, stopDuration, isOpen] = row;

    // 如果有车次信息，更新当前上下文
    if (trainCode) {
        currentTrainCode = trainCode;
        currentStart = start;
        currentEnd = end;
        currentStartTime = startTime;
        currentEndTime = endTime;
        currentType = getTrainType(type);
    }

    if (!currentTrainCode || !stationName) continue;

    const arriveStr = excelTimeToStr(arriveTime);
    const departStr = excelTimeToStr(departTime);
    const isFirst = stationName === currentStart ? 1 : 0;
    const isLast = stationName === currentEnd ? 1 : 0;

    scheduleInserts.push({
        trainCode: currentTrainCode,
        start: currentStart,
        end: currentEnd,
        startTime: currentStartTime,
        endTime: currentEndTime,
        type: currentType,
        stopOrder: stopOrder,
        stationName: stationName,
        arriveTime: arriveStr,
        departTime: departStr,
        stopDuration: stopDuration === '----' ? 0 : (parseInt(stopDuration) || 0),
        isStart: isFirst,
        isEnd: isLast,
        isOpen: isOpen === true || isOpen === 'true' ? 1 : 0
    });
}

console.log(`解析到 ${scheduleInserts.length} 条时刻表记录`);

// ============= 生成SQL语句 =============
console.log('\n正在生成SQL...');

// 生成车站SQL（更新现有车站的经纬度）
let stationSql = `-- 车站数据更新（来自12306时刻表）\n`;
stationSql += `-- 共 ${stationInserts.length} 个车站\n\n`;
stationSql += `USE railway_system;\n\n`;

for (const s of stationInserts) {
    stationSql += `UPDATE stations SET station_pinyin='${s.pinyin}', station_short_pinyin='${s.letter}' WHERE station_code='${s.code}';\n`;
}

// 生成车次SQL
let trainSql = `-- 车次数据导入\n`;
trainSql += `-- 共 ${trainInserts.length} 个车次\n\n`;
trainSql += `USE railway_system;\n\n`;
trainSql += `INSERT INTO trains (train_code, train_type, start_station_name, end_station_name, start_time, end_time, total_time, status) VALUES\n`;

const trainValues = trainInserts.map(t => {
    return `('${t.code}', '${t.type}', '${t.start}', '${t.end}', '${t.startTime}', '${t.endTime}', ${t.totalMinutes}, 1)`;
});
trainSql += trainValues.join(',\n') + ';\n';
trainSql += `\nSELECT COUNT(*) AS train_count FROM trains;\n`;

// 生成时刻表SQL
let scheduleSql = `-- 时刻表数据导入\n`;
scheduleSql += `-- 共 ${scheduleInserts.length} 条记录\n\n`;
scheduleSql += `USE railway_system;\n\n`;
scheduleSql += `INSERT INTO train_stops (train_code, station_name, stop_order, arrive_time, depart_time, stop_duration, is_start, is_end, status) VALUES\n`;

const scheduleValues = scheduleInserts.map(s => {
    const arrive = s.arriveTime || 'NULL';
    const depart = s.departTime || 'NULL';
    return `('${s.trainCode}', '${s.stationName}', ${s.stopOrder}, ${arrive === 'NULL' ? 'NULL' : "'" + arrive + "'"}, ${depart === 'NULL' ? 'NULL' : "'" + depart + "'"}, ${s.stopDuration}, ${s.isStart}, ${s.isEnd}, ${s.isOpen})`;
});
scheduleSql += scheduleValues.join(',\n') + ';\n';
scheduleSql += `\nSELECT COUNT(*) AS stop_count FROM train_stops;\n`;

// 写入文件
fs.writeFileSync('D:/xmy/12306旅客管理/database/trains_update.sql', stationSql, 'utf8');
fs.writeFileSync('D:/xmy/12306旅客管理/database/trains_import.sql', trainSql, 'utf8');
fs.writeFileSync('D:/xmy/12306旅客管理/database/train_stops_import.sql', scheduleSql, 'utf8');

console.log('SQL文件已生成:');
console.log('  - D:/xmy/12306旅客管理/database/trains_update.sql (车站更新)');
console.log('  - D:/xmy/12306旅客管理/database/trains_import.sql (车次导入)');
console.log('  - D:/xmy/12306旅客管理/database/train_stops_import.sql (时刻表导入)');

// 显示示例数据
console.log('\n========== 示例数据 ==========');
console.log('\n车次示例:');
trainInserts.slice(0, 3).forEach(t => {
    console.log(`  ${t.code}: ${t.start}(${t.startTime}) -> ${t.end}(${t.endTime}) [${t.typeName}] ${t.totalMinutes}分钟`);
});

console.log('\n时刻表示例:');
scheduleInserts.slice(0, 5).forEach(s => {
    console.log(`  ${s.trainCode} 站序${s.stopOrder}: ${s.stationName} 到${s.arriveTime} 发${s.departTime}`);
});
