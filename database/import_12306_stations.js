const https = require('https');
const fs = require('fs');

// 从12306获取车站数据
function fetchStationData() {
    return new Promise((resolve, reject) => {
        https.get('https://kyfw.12306.cn/otn/resources/js/framework/station_name.js', (res) => {
            let data = '';
            res.on('data', chunk => data += chunk);
            res.on('end', () => resolve(data));
            res.on('error', reject);
        }).on('error', reject);
    });
}

// 解析车站数据
function parseStations(content) {
    // 提取 station_names ='...' 中的数据
    const match = content.match(/station_names\s*=\s*'([^']+)'/);
    if (!match) {
        throw new Error('无法解析车站数据');
    }

    const rawData = match[1];
    const stations = [];

    // 按 @ 分割
    const entries = rawData.split('@');
    for (const entry of entries) {
        if (!entry.trim()) continue;

        // 按 | 分割字段
        const parts = entry.split('|');
        if (parts.length >= 8) {
            const [pinyin, name, code, fullPinyin, shortPinyin, , areaCode, city] = parts;

            if (name && code) {
                stations.push({
                    stationCode: code,
                    stationName: name,
                    cityName: city || '',
                    stationPinyin: fullPinyin || '',
                    stationShortPinyin: shortPinyin || '',
                    province: '',
                    stationLevel: 1,
                    isHighSpeed: name.includes('高铁') || name.includes('东站') || name.includes('西站') || name.includes('南站') || name.includes('北站') ? 1 : 0,
                    status: 1
                });
            }
        }
    }

    return stations;
}

// 生成SQL
function generateSQL(stations) {
    let sql = `-- 12306车站数据导入脚本\n`;
    sql += `-- 共 ${stations.length} 个车站\n\n`;
    sql += `USE railway_system;\n\n`;
    sql += `TRUNCATE TABLE stations;\n\n`;
    sql += `INSERT INTO stations (station_code, station_name, city_name, province, station_pinyin, station_short_pinyin, station_level, is_high_speed, status) VALUES\n`;

    const values = stations.map(s => {
        return `('${s.stationCode}', '${s.stationName.replace(/'/g, "''")}', '${s.cityName.replace(/'/g, "''")}', '${s.province}', '${s.stationPinyin}', '${s.stationShortPinyin}', ${s.stationLevel}, ${s.isHighSpeed}, ${s.status})`;
    });

    sql += values.join(',\n') + ';\n\n';
    sql += `SELECT COUNT(*) AS station_count FROM stations;\n`;

    return sql;
}

// 主函数
async function main() {
    try {
        console.log('正在从12306获取车站数据...');
        const content = await fetchStationData();
        console.log('获取成功，正在解析...');

        const stations = parseStations(content);
        console.log(`解析到 ${stations.length} 个车站`);

        const sql = generateSQL(stations);

        // 写入文件
        fs.writeFileSync('D:/xmy/12306旅客管理/database/stations_12306.sql', sql, 'utf8');
        console.log('SQL文件已生成: D:/xmy/12306旅客管理/database/stations_12306.sql');

        // 同时写入import.sql方便直接使用
        fs.writeFileSync('D:/xmy/12306旅客管理/database/import_stations.sql', sql, 'utf8');
        console.log('导入文件已生成: D:/xmy/12306旅客管理/database/import_stations.sql');

    } catch (error) {
        console.error('错误:', error.message);
    }
}

main();
