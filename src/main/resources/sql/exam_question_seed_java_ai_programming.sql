-- JAVA+AI课程（course_id = 1）编程题种子数据。
-- 覆盖阶段：BASIC、SPRING、AI。
-- 编程题不写 exam_question_option，只写参考答案/代码和课程阶段关系。

start transaction;

create temporary table tmp_exam_programming_seed
(
    q_no int primary key,
    stage_name varchar(128) not null,
    difficulty varchar(32) not null,
    question_content text not null,
    answer_content text not null,
    analysis_content text not null
);

insert into tmp_exam_programming_seed
    (q_no, stage_name, difficulty, question_content, answer_content, analysis_content)
values
    (1, 'BASIC', 'EASY',
     '编写一个Java方法，接收两个int参数，返回它们的和。',
     'public int add(int a, int b) {\n    return a + b;\n}',
     '考查方法定义、参数和返回值。'),
    (2, 'BASIC', 'EASY',
     '编写一个Java方法，判断一个整数是否为偶数。',
     'public boolean isEven(int num) {\n    return num % 2 == 0;\n}',
     '考查取模运算和boolean返回值。'),
    (3, 'BASIC', 'EASY',
     '编写一个Java方法，接收一个int数组，返回数组元素之和。',
     'public int sum(int[] nums) {\n    int total = 0;\n    for (int num : nums) {\n        total += num;\n    }\n    return total;\n}',
     '考查数组遍历和累加。'),
    (4, 'BASIC', 'NORMAL',
     '编写一个Java方法，接收一个int数组，返回最大值。如果数组为空，返回0。',
     'public int max(int[] nums) {\n    if (nums == null || nums.length == 0) {\n        return 0;\n    }\n    int max = nums[0];\n    for (int num : nums) {\n        if (num > max) {\n            max = num;\n        }\n    }\n    return max;\n}',
     '考查边界判断、数组遍历和最大值更新。'),
    (5, 'BASIC', 'NORMAL',
     '编写一个Java方法，反转字符串。例如输入abc，返回cba。',
     'public String reverse(String text) {\n    if (text == null) {\n        return null;\n    }\n    return new StringBuilder(text).reverse().toString();\n}',
     '考查StringBuilder和空值处理。'),
    (6, 'BASIC', 'NORMAL',
     '编写一个Java方法，统计字符串中字符a出现的次数。',
     'public int countA(String text) {\n    if (text == null) {\n        return 0;\n    }\n    int count = 0;\n    for (int i = 0; i < text.length(); i++) {\n        if (text.charAt(i) == \'a\') {\n            count++;\n        }\n    }\n    return count;\n}',
     '考查字符串遍历和计数。'),
    (7, 'BASIC', 'NORMAL',
     '编写一个Java方法，判断一个字符串是否为回文字符串。',
     'public boolean isPalindrome(String text) {\n    if (text == null) {\n        return false;\n    }\n    int left = 0;\n    int right = text.length() - 1;\n    while (left < right) {\n        if (text.charAt(left) != text.charAt(right)) {\n            return false;\n        }\n        left++;\n        right--;\n    }\n    return true;\n}',
     '考查双指针思想。'),
    (8, 'BASIC', 'NORMAL',
     '编写一个Java方法，接收List<Integer>，返回去重后的List，保持原有顺序。',
     'public List<Integer> distinct(List<Integer> nums) {\n    if (nums == null) {\n        return List.of();\n    }\n    Set<Integer> seen = new LinkedHashSet<>(nums);\n    return new ArrayList<>(seen);\n}',
     '考查集合去重和LinkedHashSet保持插入顺序。'),
    (9, 'BASIC', 'HARD',
     '编写一个Java方法，统计字符串中每个字符出现的次数，返回Map<Character, Integer>。',
     'public Map<Character, Integer> countChars(String text) {\n    Map<Character, Integer> result = new HashMap<>();\n    if (text == null) {\n        return result;\n    }\n    for (int i = 0; i < text.length(); i++) {\n        char ch = text.charAt(i);\n        result.put(ch, result.getOrDefault(ch, 0) + 1);\n    }\n    return result;\n}',
     '考查HashMap统计频次。'),
    (10, 'BASIC', 'HARD',
     '编写一个Java方法，实现冒泡排序，返回升序排列后的数组。',
     'public int[] bubbleSort(int[] nums) {\n    if (nums == null) {\n        return new int[0];\n    }\n    int[] arr = Arrays.copyOf(nums, nums.length);\n    for (int i = 0; i < arr.length - 1; i++) {\n        for (int j = 0; j < arr.length - 1 - i; j++) {\n            if (arr[j] > arr[j + 1]) {\n                int temp = arr[j];\n                arr[j] = arr[j + 1];\n                arr[j + 1] = temp;\n            }\n        }\n    }\n    return arr;\n}',
     '考查基础排序和数组拷贝。'),

    (11, 'SPRING', 'EASY',
     '编写一个Spring Boot Controller接口，GET /hello 返回字符串 hello。',
     '@RestController\npublic class HelloController {\n    @GetMapping(\"/hello\")\n    public String hello() {\n        return \"hello\";\n    }\n}',
     '考查@RestController和@GetMapping。'),
    (12, 'SPRING', 'EASY',
     '编写一个DTO类UserCreateReq，包含username和password字段，并要求两个字段不能为空。',
     'public class UserCreateReq {\n    @NotBlank(message = \"用户名不能为空\")\n    private String username;\n\n    @NotBlank(message = \"密码不能为空\")\n    private String password;\n}',
     '考查请求DTO和@NotBlank校验。'),
    (13, 'SPRING', 'NORMAL',
     '编写一个Controller方法，接收路径参数id，返回该id字符串。',
     '@GetMapping(\"/users/{id}\")\npublic String getUserId(@PathVariable Long id) {\n    return String.valueOf(id);\n}',
     '考查@PathVariable使用。'),
    (14, 'SPRING', 'NORMAL',
     '编写一个Controller方法，接收请求体UserCreateReq，并返回成功响应。',
     '@PostMapping(\"/users\")\npublic ApiResponse<Void> createUser(@Valid @RequestBody UserCreateReq req) {\n    return ApiResponse.success(null);\n}',
     '考查@RequestBody、@Valid和统一响应。'),
    (15, 'SPRING', 'NORMAL',
     '编写一个Service方法，判断用户名是否为空，为空时抛出BusinessException。',
     'public void validateUsername(String username) {\n    if (!StringUtils.hasText(username)) {\n        throw BusinessException.ParamsError.newInstance(\"用户名不能为空\");\n    }\n}',
     '考查业务校验和异常抛出。'),
    (16, 'SPRING', 'NORMAL',
     '编写一个MyBatis Mapper接口方法，用于根据id查询用户。',
     'public interface UserMapper {\n    UserEntity selectById(@Param(\"id\") Long id);\n}',
     '考查Mapper接口和@Param。'),
    (17, 'SPRING', 'NORMAL',
     '编写一段MyBatis XML查询，根据id查询sys_user表。',
     '<select id=\"selectById\" resultMap=\"UserMap\">\n    select id, username, nickname, status\n    from sys_user\n    where id = #{id}\n</select>',
     '考查MyBatis select XML写法。'),
    (18, 'SPRING', 'NORMAL',
     '编写一个分页查询Service片段，使用PageHelper分页后查询主表。',
     'int pageNum = req.getPageNum() == null ? 1 : req.getPageNum();\nint pageSize = req.getPageSize() == null ? 10 : req.getPageSize();\nPageHelper.startPage(pageNum, pageSize);\nList<UserEntity> list = userMapper.selectPage(query);\nPageInfo<UserEntity> pageInfo = new PageInfo<>(list);',
     '考查PageHelper必须紧跟主查询。'),
    (19, 'SPRING', 'HARD',
     '编写一个事务方法，创建订单并保存明细，任一失败时回滚。',
     '@Transactional(rollbackFor = Exception.class)\npublic Long createOrder(OrderEntity order, List<OrderItemEntity> items) {\n    orderMapper.insert(order);\n    for (OrderItemEntity item : items) {\n        item.setOrderId(order.getId());\n    }\n    orderItemMapper.insertBatch(items);\n    return order.getId();\n}',
     '考查事务边界和主从表保存。'),
    (20, 'SPRING', 'HARD',
     '编写一个动态SQL片段，按username和status可选条件查询用户。',
     '<select id=\"selectPage\" resultMap=\"UserMap\">\n    select id, username, nickname, status\n    from sys_user\n    <where>\n        <if test=\"username != null and username != \'\'\">\n            and username like concat(\'%\', #{username}, \'%\')\n        </if>\n        <if test=\"status != null and status != \'\'\">\n            and status = #{status}\n        </if>\n    </where>\n    order by id desc\n</select>',
     '考查MyBatis动态where和条件判断。'),

    (21, 'AI', 'EASY',
     '编写一个Java方法，接收用户问题和知识片段，拼接成一个简单Prompt。',
     'public String buildPrompt(String question, String context) {\n    return \"请根据以下资料回答问题：\\n\" + context + \"\\n问题：\" + question;\n}',
     '考查Prompt基本拼接。'),
    (22, 'AI', 'EASY',
     '编写一个Java方法，判断文本是否为空或全是空白。',
     'public boolean isBlankText(String text) {\n    return text == null || text.trim().isEmpty();\n}',
     '考查文本预处理的基础空值判断。'),
    (23, 'AI', 'NORMAL',
     '编写一个Java方法，按换行符切分文本，并过滤空行。',
     'public List<String> splitLines(String text) {\n    if (text == null) {\n        return List.of();\n    }\n    return Arrays.stream(text.split(\"\\\\n\"))\n            .map(String::trim)\n            .filter(line -> !line.isEmpty())\n            .toList();\n}',
     '考查文本清洗和Stream过滤。'),
    (24, 'AI', 'NORMAL',
     '编写一个Java方法，计算两个double数组的点积。',
     'public double dot(double[] a, double[] b) {\n    if (a == null || b == null || a.length != b.length) {\n        throw new IllegalArgumentException(\"向量长度不一致\");\n    }\n    double sum = 0;\n    for (int i = 0; i < a.length; i++) {\n        sum += a[i] * b[i];\n    }\n    return sum;\n}',
     '考查向量计算基础。'),
    (25, 'AI', 'NORMAL',
     '编写一个Java方法，计算double数组的L2范数。',
     'public double norm(double[] vector) {\n    if (vector == null) {\n        return 0;\n    }\n    double sum = 0;\n    for (double value : vector) {\n        sum += value * value;\n    }\n    return Math.sqrt(sum);\n}',
     '考查向量长度计算。'),
    (26, 'AI', 'NORMAL',
     '编写一个Java方法，计算两个向量的余弦相似度。',
     'public double cosine(double[] a, double[] b) {\n    double dotValue = dot(a, b);\n    double denominator = norm(a) * norm(b);\n    if (denominator == 0) {\n        return 0;\n    }\n    return dotValue / denominator;\n}',
     '考查Embedding相似度计算。'),
    (27, 'AI', 'NORMAL',
     '编写一个Java方法，从候选文本列表中找出与查询向量最相似的文本。',
     'public String topOne(double[] query, List<String> texts, List<double[]> vectors) {\n    int bestIndex = -1;\n    double bestScore = -1;\n    for (int i = 0; i < vectors.size(); i++) {\n        double score = cosine(query, vectors.get(i));\n        if (score > bestScore) {\n            bestScore = score;\n            bestIndex = i;\n        }\n    }\n    return bestIndex < 0 ? null : texts.get(bestIndex);\n}',
     '考查简单向量检索流程。'),
    (28, 'AI', 'HARD',
     '编写一个Java方法，根据召回片段列表构造RAG上下文，最多拼接maxChars个字符。',
     'public String buildContext(List<String> chunks, int maxChars) {\n    StringBuilder builder = new StringBuilder();\n    for (String chunk : chunks) {\n        if (chunk == null || chunk.isBlank()) {\n            continue;\n        }\n        if (builder.length() + chunk.length() > maxChars) {\n            break;\n        }\n        builder.append(chunk).append(\"\\n\");\n    }\n    return builder.toString();\n}',
     '考查RAG上下文长度控制。'),
    (29, 'AI', 'HARD',
     '编写一个Java方法，计算二分类预测的准确率。',
     'public double accuracy(List<Integer> labels, List<Integer> predictions) {\n    if (labels == null || predictions == null || labels.size() != predictions.size() || labels.isEmpty()) {\n        return 0;\n    }\n    int correct = 0;\n    for (int i = 0; i < labels.size(); i++) {\n        if (Objects.equals(labels.get(i), predictions.get(i))) {\n            correct++;\n        }\n    }\n    return correct * 1.0 / labels.size();\n}',
     '考查基础模型评估指标。'),
    (30, 'AI', 'HARD',
     '编写一个Java方法，按分数从高到低返回Top K文本。',
     'public List<String> topK(List<String> texts, List<Double> scores, int k) {\n    return IntStream.range(0, texts.size())\n            .boxed()\n            .sorted((i, j) -> Double.compare(scores.get(j), scores.get(i)))\n            .limit(k)\n            .map(texts::get)\n            .toList();\n}',
     '考查排序、Top K和检索结果重排。');

insert into exam_question
    (question_type, question_content, answer_content, analysis_content, difficulty, status, created_at, updated_at)
select
    'PROGRAMMING',
    question_content,
    answer_content,
    analysis_content,
    difficulty,
    'ENABLED',
    now(),
    now()
from tmp_exam_programming_seed t
where not exists (
    select 1
    from exam_question q
    where q.question_content = t.question_content
);

insert ignore into exam_question_course (question_id, course_id, stage_name, created_at)
select q.id, 1, t.stage_name, now()
from tmp_exam_programming_seed t
join exam_question q on q.question_content = t.question_content;

drop temporary table tmp_exam_programming_seed;

commit;
