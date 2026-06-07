const XLSX = require('xlsx');
const fs = require('fs');

// 读取Excel文件
const workbook = XLSX.readFile('D:/xmy/12306旅客管理/Parse12306/output/全国高速列车时刻表_20160310.xlsx');

console.log('Sheet名称:', workbook.SheetNames);

// 查看每个sheet的结构
for (const sheetName of workbook.SheetNames) {
    if (sheetName === '简介') continue;

    console.log(`\n========== ${sheetName} ==========`);
    const worksheet = workbook.Sheets[sheetName];
    const jsonData = XLSX.utils.sheet_to_json(worksheet, { header: 1 });

    console.log('总行数:', jsonData.length);
    console.log('前5行:');
    for (let i = 0; i < Math.min(5, jsonData.length); i++) {
        console.log(`  ${i}: ${JSON.stringify(jsonData[i])}`);
    }
}
