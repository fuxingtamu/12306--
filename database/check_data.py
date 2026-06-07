# -*- coding: utf-8 -*-
import subprocess

def run_query(sql):
    result = subprocess.run(
        ['mysql', '-u', 'root', '-p123456', 'railway_system', '-e', sql],
        capture_output=True, text=True, encoding='utf-8'
    )
    return result.stdout

print("=" * 60)
print("数据库检查报告")
print("=" * 60)

# 1. 检查车次数量
print("\n1. 车次表 (trains):")
print(run_query("SELECT COUNT(*) as count FROM trains;"))

# 2. 检查经停站数量
print("\n2. 经停站表 (train_stops):")
print(run_query("SELECT COUNT(*) as count FROM train_stops;"))

# 3. 检查 G1741/G1742
print("\n3. 验证直达车次 G1741/G1742:")
print(run_query("SELECT train_code, train_type, start_station_name, end_station_name, start_time, end_time FROM trains WHERE train_code IN ('G1741', 'G1742');"))

# 4. 检查 G1741 的经停站
print("\n4. G1741 经停站:")
print(run_query("SELECT stop_order, station_name, arrive_time, depart_time FROM train_stops WHERE train_code = 'G1741' ORDER BY stop_order;"))

# 5. 检查车站数量
print("\n5. 车站表 (stations):")
print(run_query("SELECT COUNT(*) as count FROM stations;"))

# 6. 检查徐州东到长沙南的车次
print("\n6. 查询 徐州东 -> 长沙南 的车次:")
sql = """
SELECT DISTINCT t.train_code, t.train_type, t.start_station_name, t.end_station_name, t.start_time, t.end_time
FROM trains t
INNER JOIN train_stops ts1 ON t.train_code = ts1.train_code AND ts1.station_name = '徐州东'
INNER JOIN train_stops ts2 ON t.train_code = ts2.train_code AND ts2.station_name = '长沙南'
WHERE ts1.stop_order < ts2.stop_order
LIMIT 10;
"""
print(run_query(sql))

print("=" * 60)
print("检查完成!")
print("=" * 60)
