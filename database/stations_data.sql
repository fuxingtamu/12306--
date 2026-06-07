-- 12306车站数据初始化脚本
-- 包含主要高铁站和火车站数据

USE railway_system;

-- 插入车站数据
INSERT INTO stations (station_code, station_name, city_name, province, station_pinyin, station_short_pinyin, station_level, is_high_speed, status) VALUES
-- 华北地区
('BJP', '北京', '北京市', '北京', 'beijing', 'bj', 1, 1, 1),
('BJN', '北京南', '北京市', '北京', 'beijingnan', 'bjn', 1, 1, 1),
('BJC', '北京朝阳', '北京市', '北京', 'beijingchaoyang', 'bjc', 1, 1, 1),
('TJN', '天津', '天津市', '天津', 'tianjin', 'tj', 1, 1, 1),
('TJN', '天津西', '天津市', '天津', 'tianjinxi', 'tjx', 1, 1, 1),
('SJP', '石家庄', '石家庄市', '河北', 'shijiazhuang', 'sjz', 1, 1, 1),
('TYD', '太原', '太原市', '山西', 'taiyuan', 'ty', 1, 1, 1),
('HKD', '呼和浩特', '呼和浩特市', '内蒙古', 'huhehaote', 'hhht', 1, 1, 1),

-- 华东地区
('SHH', '上海', '上海市', '上海', 'shanghai', 'sh', 1, 1, 1),
('SHN', '上海虹桥', '上海市', '上海', 'shanghaihongqiao', 'shhq', 1, 1, 1),
('NJH', '南京', '南京市', '江苏', 'nanjing', 'nj', 1, 1, 1),
('NJX', '南京南', '南京市', '江苏', 'nanjingnan', 'njn', 1, 1, 1),
('HZH', '杭州', '杭州市', '浙江', 'hangzhou', 'hz', 1, 1, 1),
('HZH', '杭州东', '杭州市', '浙江', 'hangzhoudong', 'hzd', 1, 1, 1),
('SZH', '苏州', '苏州市', '江苏', 'suzhou', 'sz', 1, 1, 1),
('WXD', '无锡', '无锡市', '江苏', 'wuxi', 'wx', 1, 1, 1),
('CZN', '常州', '常州市', '江苏', 'changzhou', 'cz', 1, 1, 1),
('ZJN', '镇江', '镇江市', '江苏', 'zhenjiang', 'zj', 1, 1, 1),
('NJN', '南京南', '南京市', '江苏', 'nanjingnan', 'njn', 1, 1, 1),
('HZD', '杭州东', '杭州市', '浙江', 'hangzhoudong', 'hzd', 1, 1, 1),
('NBX', '宁波', '宁波市', '浙江', 'ningbo', 'nb', 1, 1, 1),
('WZW', '温州', '温州市', '浙江', 'wenzhou', 'wz', 1, 1, 1),
('FZS', '福州', '福州市', '福建', 'fuzhou', 'fz', 1, 1, 1),
('XMS', '厦门', '厦门市', '福建', 'xiamen', 'xm', 1, 1, 1),
('JNX', '济南', '济南市', '山东', 'jinan', 'jn', 1, 1, 1),
('JNW', '济南西', '济南市', '山东', 'jinanxi', 'jnx', 1, 1, 1),
('QDW', '青岛', '青岛市', '山东', 'qingdao', 'qd', 1, 1, 1),
('QDX', '青岛西', '青岛市', '山东', 'qingdaoxi', 'qdx', 1, 1, 1),

-- 华中地区
('WHN', '武汉', '武汉市', '湖北', 'wuhan', 'wh', 1, 1, 1),
('WHX', '武汉站', '武汉市', '湖北', 'wuhanzhan', 'whz', 1, 1, 1),
('CSQ', '长沙', '长沙市', '湖南', 'changsha', 'cs', 1, 1, 1),
('CSX', '长沙南', '长沙市', '湖南', 'changshanan', 'csn', 1, 1, 1),
('ZZF', '郑州', '郑州市', '河南', 'zhengzhou', 'zz', 1, 1, 1),
('ZZD', '郑州东', '郑州市', '河南', 'zhengzhoudong', 'zzd', 1, 1, 1),
('XAY', '西安', '西安市', '陕西', 'xian', 'xa', 1, 1, 1),
('XAE', '西安北', '西安市', '陕西', 'xianbei', 'xab', 1, 1, 1),
('NCX', '南昌', '南昌市', '江西', 'nanchang', 'nc', 1, 1, 1),
('NCS', '南昌西', '南昌市', '江西', 'nanchangxi', 'ncx', 1, 1, 1),
('HFE', '合肥', '合肥市', '安徽', 'hefei', 'hf', 1, 1, 1),
('HFN', '合肥南', '合肥市', '安徽', 'hefeinan', 'hfn', 1, 1, 1),

-- 华南地区
('GZQ', '广州', '广州市', '广东', 'guangzhou', 'gz', 1, 1, 1),
('GZN', '广州南', '广州市', '广东', 'guangzhounan', 'gzn', 1, 1, 1),
('GZS', '广州东', '广州市', '广东', 'guangzhoudong', 'gzd', 1, 1, 1),
('SZH', '深圳', '深圳市', '广东', 'shenzhen', 'sz', 1, 1, 1),
('SZB', '深圳北', '深圳市', '广东', 'shenzhenbei', 'szb', 1, 1, 1),
('SZX', '深圳西', '深圳市', '广东', 'shenzhenxi', 'szx', 1, 1, 1),
('NNG', '南宁', '南宁市', '广西', 'nanning', 'nn', 1, 1, 1),
('GLB', '桂林', '桂林市', '广西', 'guilin', 'gl', 1, 1, 1),
('HCZ', '合肥', '海口市', '海南', 'haikou', 'hk', 1, 1, 1),

-- 西南地区
('CDX', '成都东', '成都市', '四川', 'chengdudong', 'cdd', 1, 1, 1),
('CDX', '成都', '成都市', '四川', 'chengdu', 'cd', 1, 1, 1),
('CDW', '成都西', '成都市', '四川', 'chengduxi', 'cdx', 1, 1, 1),
('CQN', '重庆北', '重庆市', '重庆', 'chongqingbei', 'cqb', 1, 1, 1),
('CQX', '重庆西', '重庆市', '重庆', 'chongqingxi', 'cqx', 1, 1, 1),
('KMM', '昆明', '昆明市', '云南', 'kunming', 'km', 1, 1, 1),
('KUN', '昆明南', '昆明市', '云南', 'kunmingnan', 'kmn', 1, 1, 1),
('GYW', '贵阳', '贵阳市', '贵州', 'guiyang', 'gy', 1, 1, 1),

-- 东北地区
('SYX', '沈阳', '沈阳市', '辽宁', 'shenyang', 'sy', 1, 1, 1),
('SYT', '沈阳北', '沈阳市', '辽宁', 'shenyangbei', 'syb', 1, 1, 1),
('DLC', '大连', '大连市', '辽宁', 'dalian', 'dl', 1, 1, 1),
('DLX', '大连北', '大连市', '辽宁', 'dalianbei', 'dlb', 1, 1, 1),
('CCT', '长春', '长春市', '吉林', 'changchun', 'cc', 1, 1, 1),
('HHH', '哈尔滨', '哈尔滨市', '黑龙江', 'haerbin', 'heb', 1, 1, 1),
('HBX', '哈尔滨西', '哈尔滨市', '黑龙江', 'haerbinxi', 'hbx', 1, 1, 1),

-- 西北地区
('XAN', '西安', '西安市', '陕西', 'xian', 'xa', 1, 1, 1),
('XAG', '西安北', '西安市', '陕西', 'xianbei', 'xab', 1, 1, 1),
('LZW', '兰州', '兰州市', '甘肃', 'lanzhou', 'lz', 1, 1, 1),
('LZX', '兰州西', '兰州市', '甘肃', 'lanzhouxi', 'lzx', 1, 1, 1),
('XNG', '西宁', '西宁市', '青海', 'xining', 'xn', 1, 1, 1),
('YCH', '银川', '银川市', '宁夏', 'yinchuan', 'yc', 1, 1, 1),
('WLM', '乌鲁木齐', '乌鲁木齐市', '新疆', 'wulumuqi', 'wlmq', 1, 1, 1);

SELECT COUNT(*) AS station_count FROM stations;
