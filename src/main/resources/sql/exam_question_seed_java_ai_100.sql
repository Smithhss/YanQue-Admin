-- JAVA+AI课程（course_id = 1）题库种子数据：100道单选题。
-- 覆盖阶段：BASIC、SPRING、AI。
-- 执行前请确认 exam_question、exam_question_option、exam_question_course 已按最新结构创建。

start transaction;

create temporary table tmp_exam_single_choice_seed
(
    q_no int primary key,
    stage_name varchar(128) not null,
    difficulty varchar(32) not null,
    question_content text not null,
    answer_content varchar(16) not null,
    analysis_content text not null,
    option_a text not null,
    option_b text not null,
    option_c text not null,
    option_d text not null
);

insert into tmp_exam_single_choice_seed
    (q_no, stage_name, difficulty, question_content, answer_content, analysis_content, option_a, option_b, option_c, option_d)
values
    (1, 'BASIC', 'EASY', 'Java中用于定义类的关键字是？', 'A', 'class用于声明类，是Java面向对象程序的基本结构。', 'class', 'struct', 'function', 'module'),
    (2, 'BASIC', 'EASY', 'Java程序的入口方法通常是？', 'B', 'public static void main(String[] args) 是Java应用程序常见入口。', 'start方法', 'main方法', 'run方法', 'init方法'),
    (3, 'BASIC', 'EASY', '下面哪个类型用于表示整数？', 'A', 'int是Java中常用的整数基本类型。', 'int', 'boolean', 'char', 'double'),
    (4, 'BASIC', 'EASY', '下面哪个类型用于表示真假值？', 'C', 'boolean用于表示true或false。', 'byte', 'String', 'boolean', 'float'),
    (5, 'BASIC', 'EASY', 'Java中字符串常用的引用类型是？', 'D', 'String用于表示字符串对象。', 'CharArray', 'Text', 'Str', 'String'),
    (6, 'BASIC', 'EASY', '下面哪个符号用于单行注释？', 'A', 'Java中双斜杠用于单行注释。', '//', '/* */', '#', '--'),
    (7, 'BASIC', 'EASY', '下面哪个关键字用于创建对象？', 'B', 'new用于创建对象实例。', 'make', 'new', 'create', 'build'),
    (8, 'BASIC', 'NORMAL', 'Java中数组下标从几开始？', 'A', 'Java数组下标从0开始。', '0', '1', '-1', '数组长度'),
    (9, 'BASIC', 'NORMAL', '下面哪个访问修饰符表示同包和子类可访问？', 'C', 'protected允许同包类和子类访问。', 'private', 'public', 'protected', 'final'),
    (10, 'BASIC', 'NORMAL', 'private修饰的成员主要表示什么？', 'B', 'private限制成员只在当前类内部访问。', '所有类都可访问', '仅当前类可访问', '仅子类可访问', '仅同包可访问'),
    (11, 'BASIC', 'NORMAL', 'Java中用于继承类的关键字是？', 'D', 'extends用于类继承。', 'implements', 'inherits', 'instanceof', 'extends'),
    (12, 'BASIC', 'NORMAL', 'Java中用于实现接口的关键字是？', 'A', 'implements用于类实现接口。', 'implements', 'extends', 'interface', 'override'),
    (13, 'BASIC', 'NORMAL', '下面哪个集合允许元素按索引访问？', 'B', 'List支持按索引访问元素。', 'Set', 'List', 'Map', 'Queue'),
    (14, 'BASIC', 'NORMAL', 'HashMap主要存储什么结构的数据？', 'C', 'HashMap以键值对形式存储数据。', '单个值', '栈结构', '键值对', '有序数组'),
    (15, 'BASIC', 'NORMAL', '下面哪个集合不允许重复元素？', 'A', 'Set集合语义是不允许重复元素。', 'Set', 'List', 'ArrayList', 'LinkedList'),
    (16, 'BASIC', 'NORMAL', 'Java异常处理中用于捕获异常的关键字是？', 'B', 'catch用于捕获try块抛出的异常。', 'throw', 'catch', 'error', 'finally'),
    (17, 'BASIC', 'NORMAL', 'finally代码块的特点通常是？', 'D', 'finally通常用于释放资源，在异常处理流程结束前执行。', '只在无异常时执行', '只在编译期执行', '永远不会执行', '通常都会执行'),
    (18, 'BASIC', 'NORMAL', '面向对象三大特征不包括下面哪一项？', 'C', '常见三大特征是封装、继承、多态，不包括编译。', '封装', '继承', '编译', '多态'),
    (19, 'BASIC', 'NORMAL', '方法重载主要看什么不同？', 'A', '方法重载要求方法名相同但参数列表不同。', '参数列表', '返回值名称', '包名', '注释内容'),
    (20, 'BASIC', 'NORMAL', '方法重写通常发生在什么关系中？', 'B', '重写通常发生在父子类继承关系中。', '两个无关类', '父子类', '同一个方法内部', '数据库表之间'),
    (21, 'BASIC', 'NORMAL', 'static修饰的方法属于谁？', 'C', 'static成员属于类本身，而不是某个对象实例。', '某个对象', '某个线程', '类', '包'),
    (22, 'BASIC', 'NORMAL', 'final修饰变量时通常表示什么？', 'A', 'final变量赋值后不可再次修改引用或基本值。', '不可重新赋值', '必须为空', '必须继承', '自动回收'),
    (23, 'BASIC', 'NORMAL', '接口中的抽象方法默认具有什么访问级别？', 'D', '接口抽象方法默认是public abstract。', 'private', 'protected', 'default only', 'public'),
    (24, 'BASIC', 'NORMAL', '下面哪个关键字用于判断对象类型？', 'B', 'instanceof用于判断对象是否属于某个类型。', 'typeof', 'instanceof', 'isclass', 'check'),
    (25, 'BASIC', 'NORMAL', 'StringBuilder相比String更适合哪种场景？', 'A', '大量字符串拼接时StringBuilder可减少中间对象。', '频繁拼接字符串', '表示真假值', '定义接口', '捕获异常'),
    (26, 'BASIC', 'NORMAL', 'Java中包声明使用哪个关键字？', 'C', 'package用于声明当前类所在包。', 'import', 'namespace', 'package', 'module'),
    (27, 'BASIC', 'NORMAL', 'Java中导入其他包类使用哪个关键字？', 'D', 'import用于导入其他包中的类。', 'include', 'require', 'using', 'import'),
    (28, 'BASIC', 'NORMAL', '构造方法的名称必须和什么一致？', 'B', '构造方法名称必须和类名一致。', '包名', '类名', '文件夹名', '返回值类型'),
    (29, 'BASIC', 'NORMAL', '抽象类使用哪个关键字声明？', 'A', 'abstract可用于声明抽象类或抽象方法。', 'abstract', 'static', 'final', 'native'),
    (30, 'BASIC', 'NORMAL', 'Java中所有类默认直接或间接继承哪个类？', 'C', 'Object是Java类层次结构的根类。', 'Class', 'System', 'Object', 'Thread'),
    (31, 'BASIC', 'HARD', 'HashMap查询平均时间复杂度通常认为是？', 'B', '哈希分布良好时HashMap查询平均复杂度接近O(1)。', 'O(n)', 'O(1)', 'O(log n)', 'O(n^2)'),
    (32, 'BASIC', 'HARD', 'ArrayList底层主要基于什么实现？', 'A', 'ArrayList底层主要使用数组存储元素。', '数组', '链表', '红黑树', '哈希表'),
    (33, 'BASIC', 'HARD', 'LinkedList更适合哪类操作场景？', 'C', 'LinkedList在已定位节点附近插入删除较方便。', '频繁随机访问', '数学计算', '频繁插入删除', '常量池管理'),
    (34, 'BASIC', 'HARD', 'Java中 == 比较引用类型时主要比较什么？', 'D', '引用类型使用==比较的是引用地址是否相同。', '对象内容', '字段数量', '类名', '引用地址'),
    (35, 'BASIC', 'HARD', 'equals方法默认来自Object时比较什么？', 'A', 'Object默认equals与==类似，比较对象引用。', '对象引用', 'JSON内容', '数据库主键', '注释文本'),
    (36, 'SPRING', 'EASY', 'Spring中用于声明组件类的常用注解是？', 'A', '@Component用于声明Spring容器管理的组件。', '@Component', '@Column', '@Table', '@EntityId'),
    (37, 'SPRING', 'EASY', 'Spring MVC中声明控制器常用注解是？', 'B', '@RestController常用于声明REST接口控制器。', '@Service', '@RestController', '@Mapper', '@BeanOnly'),
    (38, 'SPRING', 'EASY', 'Spring中声明业务服务类常用注解是？', 'C', '@Service通常用于标识业务服务组件。', '@RequestBody', '@RepositoryOnly', '@Service', '@Path'),
    (39, 'SPRING', 'EASY', 'Spring Boot启动类通常使用哪个注解？', 'D', '@SpringBootApplication是Spring Boot启动类常用组合注解。', '@Boot', '@Application', '@SpringMain', '@SpringBootApplication'),
    (40, 'SPRING', 'EASY', '下面哪个注解用于GET请求映射？', 'A', '@GetMapping用于处理HTTP GET请求。', '@GetMapping', '@PostMapping', '@PutMapping', '@DeleteMapping'),
    (41, 'SPRING', 'EASY', '下面哪个注解用于POST请求映射？', 'B', '@PostMapping用于处理HTTP POST请求。', '@GetMapping', '@PostMapping', '@PutMapping', '@PatchOnly'),
    (42, 'SPRING', 'NORMAL', '@RequestBody主要用于接收什么数据？', 'C', '@RequestBody通常将请求体JSON反序列化为对象。', 'URL路径', '请求头', '请求体JSON', '服务器端口'),
    (43, 'SPRING', 'NORMAL', '@PathVariable主要用于接收什么参数？', 'A', '@PathVariable用于接收URL路径中的变量。', '路径变量', '请求体字段', 'Cookie', '数据库字段'),
    (44, 'SPRING', 'NORMAL', '@RequestParam主要用于接收什么参数？', 'D', '@RequestParam常用于接收URL查询参数或表单参数。', '类成员', '返回值', '异常对象', '请求参数'),
    (45, 'SPRING', 'NORMAL', 'Spring容器管理对象通常称为什么？', 'B', '被Spring容器管理的对象通常称为Bean。', 'Node', 'Bean', 'Packet', 'Record'),
    (46, 'SPRING', 'NORMAL', '依赖注入的主要目的是什么？', 'C', '依赖注入用于降低对象创建和依赖管理的耦合。', '提高SQL长度', '替代HTTP协议', '降低耦合', '关闭事务'),
    (47, 'SPRING', 'NORMAL', '@Autowired默认按什么注入依赖？', 'A', '@Autowired通常按类型完成依赖注入。', '类型', '文件名', '端口号', '注释内容'),
    (48, 'SPRING', 'NORMAL', '@Configuration通常表示什么？', 'D', '@Configuration表示配置类，常用于声明Bean配置。', '数据库表', '前端页面', '异常类', '配置类'),
    (49, 'SPRING', 'NORMAL', '@Bean通常写在什么类中？', 'B', '@Bean通常写在配置类中，用于向容器注册对象。', '实体类', '配置类', '枚举类', '测试报告'),
    (50, 'SPRING', 'NORMAL', 'Spring中@Transactional主要用于什么？', 'C', '@Transactional用于声明事务边界。', '路由跳转', '参数签名', '事务管理', '文件压缩'),
    (51, 'SPRING', 'NORMAL', 'MyBatis中Mapper接口主要负责什么？', 'A', 'Mapper接口用于定义数据库访问方法。', '数据库访问', '页面渲染', '用户登录动画', '线程命名'),
    (52, 'SPRING', 'NORMAL', 'MyBatis XML中select标签通常用于什么？', 'B', 'select标签用于定义查询SQL。', '插入数据', '查询数据', '删除文件', '启动服务'),
    (53, 'SPRING', 'NORMAL', 'PageHelper.startPage应放在什么位置？', 'C', 'PageHelper应紧跟在主分页查询之前调用。', '所有查询之后', '程序启动时', '主分页查询之前', 'Controller返回之后'),
    (54, 'SPRING', 'NORMAL', 'REST接口中PUT通常表示什么操作？', 'D', 'PUT通常用于更新资源。', '查询', '登录', '删除', '更新'),
    (55, 'SPRING', 'NORMAL', 'REST接口中DELETE通常表示什么操作？', 'A', 'DELETE通常用于删除资源。', '删除', '新增', '分页', '导入'),
    (56, 'SPRING', 'NORMAL', '统一响应对象的好处是什么？', 'B', '统一响应可让前端稳定处理code、message和data。', '减少数据库表数量', '稳定前后端响应结构', '替代业务校验', '关闭跨域'),
    (57, 'SPRING', 'NORMAL', '参数校验中@NotBlank主要用于校验什么？', 'C', '@NotBlank用于校验字符串不为null且去空白后不为空。', '数字大于0', '日期格式', '字符串非空白', '文件存在'),
    (58, 'SPRING', 'NORMAL', '参数校验中@NotNull主要用于校验什么？', 'D', '@NotNull用于校验对象不能为null。', '字符串长度', '邮箱格式', '必须唯一', '不能为null'),
    (59, 'SPRING', 'NORMAL', 'Controller层最适合承担什么职责？', 'A', 'Controller适合处理HTTP入口、参数校验和DTO边界。', 'HTTP入口和参数校验', '复杂SQL拼接', '机器学习训练', '操作系统调度'),
    (60, 'SPRING', 'NORMAL', 'Service层通常不应该直接依赖什么？', 'B', '领域Service通常应避免直接依赖Controller请求DTO。', '实体或BO', 'Controller请求DTO', 'Mapper接口', '领域对象'),
    (61, 'SPRING', 'NORMAL', 'Mapper XML中的where标签有什么作用？', 'C', 'where标签可帮助动态SQL处理多余的and。', '启动事务', '生成前端页面', '拼接查询条件', '创建线程池'),
    (62, 'SPRING', 'NORMAL', 'Spring Boot配置文件常见名称是？', 'D', 'application.yaml或application.properties是常见配置文件。', 'main.java', 'pom.lock', 'index.html', 'application.yaml'),
    (63, 'SPRING', 'HARD', 'AOP主要适合处理哪类逻辑？', 'A', 'AOP适合日志、权限、事务等横切关注点。', '横切关注点', '实体字段命名', '页面排版', '图片裁剪'),
    (64, 'SPRING', 'HARD', '拦截器Interceptor常用于什么场景？', 'B', '拦截器常用于登录校验、权限校验、签名校验等请求前后处理。', '编译Java字节码', '请求前后处理', '数据库备份', '数组排序'),
    (65, 'SPRING', 'HARD', 'Spring事务默认对哪类异常更容易触发回滚？', 'C', 'Spring声明式事务默认对运行时异常和Error回滚。', '所有受检异常', '普通返回值', '运行时异常', '日志文本'),
    (66, 'SPRING', 'HARD', 'MyBatis中resultMap主要解决什么问题？', 'D', 'resultMap用于定义列名和对象属性之间的映射。', '接口签名', 'HTTP状态码', '页面路由', '结果映射'),
    (67, 'SPRING', 'HARD', '避免列表SQL复杂join的一种做法是什么？', 'A', '先查主表再批量查询关联数据，可降低分页和SQL复杂度。', '先查主表再批量组装关联字段', '所有字段都写成JSON', '每行前端再查一次', '删除分页功能'),
    (68, 'SPRING', 'HARD', '接口幂等通常关注什么？', 'B', '幂等关注同一请求重复执行不会造成重复副作用。', '页面颜色一致', '重复请求副作用控制', '日志文件大小', 'CSS命名'),
    (69, 'SPRING', 'HARD', '权限校验中AntPathMatcher适合做什么？', 'C', 'AntPathMatcher可用于匹配带通配或路径变量的接口路径。', '解析Excel', '生成Token', '匹配接口路径', '压缩图片'),
    (70, 'SPRING', 'HARD', 'HMAC签名校验主要用于防止什么问题？', 'D', 'HMAC签名可帮助校验请求未被篡改并来自可信客户端。', 'SQL字段为空', '页面过宽', '类名太长', '请求被篡改'),
    (71, 'AI', 'EASY', '人工智能中AI通常指什么？', 'A', 'AI是Artificial Intelligence的缩写，表示人工智能。', '人工智能', '自动索引', '应用接口', '数组迭代'),
    (72, 'AI', 'EASY', '机器学习主要让计算机从什么中学习规律？', 'B', '机器学习通过数据和经验学习规律。', '电源', '数据', '显示器', '键盘'),
    (73, 'AI', 'EASY', '监督学习通常需要什么数据？', 'C', '监督学习通常需要带标签的数据。', '无网络数据', '加密数据', '带标签数据', '空数据'),
    (74, 'AI', 'EASY', '分类任务的输出通常是什么？', 'D', '分类任务输出离散类别。', '图片大小', '训练时间', '连续金额', '离散类别'),
    (75, 'AI', 'EASY', '回归任务的输出通常是什么？', 'A', '回归任务通常预测连续数值。', '连续数值', '固定类别', 'HTTP方法', '代码行号'),
    (76, 'AI', 'NORMAL', '训练集主要用于什么？', 'B', '训练集用于让模型学习参数和规律。', '最终上线域名', '模型学习', '删除样本', '展示图标'),
    (77, 'AI', 'NORMAL', '测试集主要用于什么？', 'C', '测试集用于评估模型在未见数据上的效果。', '训练参数', '生成日志', '评估效果', '配置端口'),
    (78, 'AI', 'NORMAL', '过拟合通常表示什么？', 'D', '过拟合表示模型过度记住训练数据，泛化能力较差。', '模型太小', '数据完全为空', '没有训练', '训练表现好但泛化差'),
    (79, 'AI', 'NORMAL', '欠拟合通常表示什么？', 'A', '欠拟合表示模型能力不足，训练数据都学不好。', '训练数据也学不好', '模型记住所有样本', '测试准确率必定100%', '不需要特征'),
    (80, 'AI', 'NORMAL', '特征工程主要关注什么？', 'B', '特征工程关注从原始数据构造更有效的输入特征。', '服务器关机', '构造有效输入特征', '前端按钮颜色', '删除模型文件'),
    (81, 'AI', 'NORMAL', '向量化表示的核心作用是什么？', 'C', '向量化把文本、图片等数据转为模型可计算的数值向量。', '压缩数据库', '替换Java语法', '转为可计算数值向量', '关闭训练流程'),
    (82, 'AI', 'NORMAL', 'Embedding通常表示什么？', 'D', 'Embedding是把对象映射到低维或稠密向量空间的表示。', '接口路径', '异常堆栈', '页面菜单', '稠密向量表示'),
    (83, 'AI', 'NORMAL', '大语言模型常用于什么任务？', 'A', '大语言模型常用于文本生成、问答、摘要等语言任务。', '文本生成和问答', '硬盘分区', '电路焊接', '物理联网'),
    (84, 'AI', 'NORMAL', 'Prompt在大模型使用中主要是什么？', 'B', 'Prompt是提供给模型的指令、问题或上下文。', '数据库索引', '模型输入指令', '显卡驱动', '操作系统进程'),
    (85, 'AI', 'NORMAL', 'RAG中的检索步骤主要做什么？', 'C', 'RAG先从知识库检索相关内容，再辅助生成回答。', '删除知识库', '训练操作系统', '查找相关知识', '关闭模型'),
    (86, 'AI', 'NORMAL', '向量数据库常用于支持什么能力？', 'D', '向量数据库常用于相似度检索。', '页面布局', '短信发送', '日志打印', '相似度检索'),
    (87, 'AI', 'NORMAL', '余弦相似度通常用于衡量什么？', 'A', '余弦相似度可衡量两个向量方向的相似程度。', '向量相似程度', '文件大小', 'CPU温度', 'HTTP状态'),
    (88, 'AI', 'NORMAL', '模型评估中的准确率表示什么？', 'B', '准确率表示预测正确样本占总样本的比例。', '请求成功次数', '预测正确比例', '内存剩余比例', '页面加载速度'),
    (89, 'AI', 'NORMAL', '混淆矩阵常用于分析什么任务？', 'C', '混淆矩阵常用于分类模型效果分析。', '数据库建表', '图片压缩', '分类模型', '线程调度'),
    (90, 'AI', 'NORMAL', '数据清洗主要解决什么问题？', 'D', '数据清洗处理缺失、错误、重复等数据质量问题。', '模型上线', '页面刷新', '接口鉴权', '数据质量问题'),
    (91, 'AI', 'NORMAL', 'Token在大语言模型中通常指什么？', 'A', 'Token是模型处理文本时的基本片段单位。', '文本片段单位', '数据库连接', '图像像素', '线程名称'),
    (92, 'AI', 'NORMAL', '上下文窗口限制影响什么？', 'B', '上下文窗口限制模型一次可处理的输入和输出长度。', '电脑屏幕大小', '模型可处理文本长度', '数据库表数量', '网络端口数量'),
    (93, 'AI', 'NORMAL', '温度temperature参数通常影响什么？', 'C', 'temperature越高，生成结果通常越随机。', '服务器温度', '请求路径', '生成随机性', '数据库密码'),
    (94, 'AI', 'NORMAL', '微调Fine-tuning主要指什么？', 'D', '微调是在已有模型基础上用特定数据继续训练。', '压缩文件', '修改浏览器', '重启服务器', '继续训练模型'),
    (95, 'AI', 'HARD', '梯度下降主要用于什么？', 'A', '梯度下降用于通过优化损失函数来更新模型参数。', '优化模型参数', '删除训练集', '创建HTTP接口', '设计页面菜单'),
    (96, 'AI', 'HARD', '损失函数的作用是什么？', 'B', '损失函数用于度量预测结果与真实结果的差距。', '记录登录日志', '衡量预测误差', '压缩响应体', '生成SQL表'),
    (97, 'AI', 'HARD', '正则化常用于缓解什么问题？', 'C', '正则化通过约束模型复杂度来缓解过拟合。', '网络断开', '编译错误', '过拟合', '页面空白'),
    (98, 'AI', 'HARD', '召回率更关注什么？', 'D', '召回率关注真实正例中被正确找出的比例。', '所有预测为正的比例', 'CPU占用率', '页面访问量', '真实正例找回比例'),
    (99, 'AI', 'HARD', '精确率更关注什么？', 'A', '精确率关注预测为正的样本中真正为正的比例。', '预测为正中的正确比例', '所有样本数量', '模型文件大小', '接口耗时'),
    (100, 'AI', 'HARD', '在RAG系统中，召回内容质量差最可能导致什么？', 'B', 'RAG依赖检索内容，召回质量差会影响生成答案质量。', '数据库自动扩容', '回答依据不足或错误', '浏览器无法启动', 'Java语法失效');

insert into exam_question
    (question_type, question_content, answer_content, analysis_content, difficulty, status, created_at, updated_at)
select
    'SINGLE',
    question_content,
    answer_content,
    analysis_content,
    difficulty,
    'ENABLED',
    now(),
    now()
from tmp_exam_single_choice_seed t
where not exists (
    select 1
    from exam_question q
    where q.question_content = t.question_content
);

insert ignore into exam_question_course (question_id, course_id, stage_name, created_at)
select q.id, 1, t.stage_name, now()
from tmp_exam_single_choice_seed t
join exam_question q on q.question_content = t.question_content;

insert ignore into exam_question_option (question_id, option_key, option_content, created_at, updated_at)
select q.id, 'A', t.option_a, now(), now()
from tmp_exam_single_choice_seed t
join exam_question q on q.question_content = t.question_content;

insert ignore into exam_question_option (question_id, option_key, option_content, created_at, updated_at)
select q.id, 'B', t.option_b, now(), now()
from tmp_exam_single_choice_seed t
join exam_question q on q.question_content = t.question_content;

insert ignore into exam_question_option (question_id, option_key, option_content, created_at, updated_at)
select q.id, 'C', t.option_c, now(), now()
from tmp_exam_single_choice_seed t
join exam_question q on q.question_content = t.question_content;

insert ignore into exam_question_option (question_id, option_key, option_content, created_at, updated_at)
select q.id, 'D', t.option_d, now(), now()
from tmp_exam_single_choice_seed t
join exam_question q on q.question_content = t.question_content;

drop temporary table tmp_exam_single_choice_seed;

commit;
