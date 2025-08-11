# JetBrains Classic ASP Plugin

### 项目说明

- 这是一个 jetbrains 编辑器插件，语言：classic asp
- 插件名称：jetbrains_classic_asp_plugin
- 插件作者：ferock : ferock@gmail.com
- 插件版本：1.0.6
- 实现语言： java
- java 版本：24
- gradle 版本：8.14
- jetbrains 编辑器兼容版本：25.x
- 统一标识：ClassicASP

- 语法处理逻辑说明：UNIFIED_PROCESSOR.md

### 功能

- [X] 语法高亮（支持 Classic ASP 和 HTML 混合语法）
- [X] 代码格式化（关键字大小写、缩进、运算符空格）
- [X] 原生函数提示（Function Hints/Parameter Info）
- [X] 当前页面定义变量、方法 提示
- [ ] 语法错误提示
- [ ] 项目内函数提示
- [ ] 函数跳转
- [ ] 函数参数提示

```
文件类型识别
词法分析
语法高亮
格式化
代码洞察和代码补全
检查和快速修复
意图动作
```


### 特性说明

#### 语法高亮功能
- **ASP 语法高亮**：支持 Classic ASP 关键字、内置对象、函数等
- **HTML 语法高亮**：支持 HTML 标签、属性、文本内容等
- **混合语法高亮**：在同一个文件中同时支持 ASP 和 HTML 语法高亮
- **智能上下文识别**：在 `<% %>` ASP代码块内禁用HTML验证器，确保纯ASP代码不被误识别为HTML
- **注释高亮**：支持 ASP 注释（以 `'` 开头）
- **字符串高亮**：支持字符串内容高亮
- **数字高亮**：支持数字字面量高亮

#### 代码格式化功能
- **ASP 代码格式化**：自动格式化 ASP 代码块，包括缩进和间距
- **HTML 代码格式化**：自动格式化 HTML 标签和属性
- **混合代码格式化**：智能处理 ASP 和 HTML 混合代码的格式化
- **缩进管理**：自动管理代码缩进，保持代码结构清晰
- **间距优化**：优化代码间距，提高代码可读性

#### 代码提示功能
- **VBScript 关键词提示**：提供所有 VBScript 关键词的智能提示
- **内置函数提示**：提供 Classic ASP 内置函数的完整提示
- **内置对象提示**：提供 ASP 六大内置对象的提示
- **分类提示**：按功能分类显示关键词（内置函数、循环关键字、条件关键字等）
- **自动空格**：插入关键词后自动添加适当的空格
- **大小写规范**：提示的关键词使用标准的 Pascal 大小写格式

##### 格式化规则详细说明

**1. 关键字大小写转换**
- 所有VBScript关键字自动转换为标准Pascal大小写格式
- 例如：`if` → `If`，`then` → `Then`，`end if` → `End If`
- 支持的关键字包括：
  - 控制流：`If`, `Then`, `Else`, `End If`, `For`, `Next`, `While`, `Wend`, `Do`, `Loop`
  - 函数：`Function`, `End Function`, `Sub`, `End Sub`
  - 选择：`Select`, `Case`, `End Select`
  - 属性：`Property`, `End Property`
  - 变量：`Dim`, `Set`
  - 特殊值：`Empty`, `Nothing`, `Null`, `True`, `False`

**2. VBScript内置函数大小写**
- 所有内置函数转换为标准大小写
- 字符串函数：`Len`, `Left`, `Right`, `Mid`, `UCase`, `LCase`, `Trim`, `Replace`, `Split`, `Join`, `InStr`
- 类型检查：`IsEmpty`, `IsNothing`, `IsNull`, `IsNumeric`, `IsObject`, `IsDate`
- 类型转换：`CStr`, `CInt`, `CDbl`, `CBool`, `CDate`
- 交互函数：`MsgBox`, `InputBox`

**3. 运算符空格规则**
- **算术运算符**：两侧各添加一个空格
  - `+`, `-`, `*`, `/`, `\`, `^`, `Mod`
  - 例如：`a+b` → `a + b`，`x*y` → `x * y`
- **比较运算符**：两侧各添加一个空格
  - `=`, `<>`, `<`, `>`, `<=`, `>=`, `Is`
  - 例如：`a=b` → `a = b`，`x<>y` → `x <> y`
- **逻辑运算符**：两侧各添加一个空格
  - `Not`, `And`, `Or`, `Xor`, `Eqv`, `Imp`
  - 例如：`a And b` → `a And b`
- **字符串连接**：两侧各添加一个空格
  - `&` 连接运算符
  - 例如：`"Hello"&"World"` → `"Hello" & "World"`

**4. 缩进规则**
- **控制结构缩进**：内部语句缩进一级（4个空格）
  - `If...Then...End If` 语句块
  - `For...Next` 循环
  - `While...Wend` 循环
  - `Do...Loop` 循环
  - `Function...End Function` 函数
  - `Sub...End Sub` 过程
- **Select Case缩进**：特殊处理
  - `Case` 语句缩进一级
  - `Case` 内容缩进两级
  ```vbscript
  Select Case variable
      Case "value1"
          Response.Write "Match"
  End Select
  ```

**5. 空行规则**
- 控制结构前自动添加空行，提高代码可读性
- 适用于：`If`, `For`, `While`, `Do`, `Function`, `Sub`, `Select`, `Property`

**格式化前后对比示例：**
```vbscript
<!-- 格式化前 -->
<%
dim testVar
response.write "Hello"
if testVar="" then
response.write "Empty"
end if
while testVar<>""
response.write "Loop"
wend
%>

<!-- 格式化后 -->
<%
Dim testVar
Response.Write "Hello"

If testVar = "" Then
    Response.Write "Empty"
End If

While testVar <> ""
    Response.Write "Loop"
Wend
%>
```

#### 代码提示功能详细说明

#### keywords.yaml 文件说明
- 只要把词加入 keywords.yaml 的 keywords 分组（例如 exception: [on, error, resume, goto]），无需改代码，即会自动按关键字配色。
点号后的方法名（如 Response.Write、Server.MapPath）会按方法颜色高亮，方法清单来自 keywords.yaml 的 objects 段。
- 希望大小写格式化也跟进，可在 case_map 增加条目，例如：
> goto: GoTo
- 若发现某个对象方法未着色，把方法名加到该对象的 methods 列表即可。

- **1. VBScript 关键词提示**
- **声明关键字**：`Dim`, `Set`, `Const`, `ReDim` 等
- **控制流关键字**：`If`, `Then`, `Else`, `ElseIf`, `End If`, `Select Case`, `Case`, `End Select` 等
- **循环关键字**：`For`, `Next`, `While`, `Wend`, `Do`, `Loop`, `Each`, `In` 等
- **函数关键字**：`Function`, `Sub`, `End Function`, `End Sub`, `Call` 等
- **异常处理关键字**：`On Error`, `Resume`, `Resume Next` 等
- **逻辑运算符**：`And`, `Or`, `Not`, `Xor`, `Eqv`, `Imp` 等
- **特殊值**：`Empty`, `Nothing`, `Null`, `True`, `False` 等

**2. 内置函数提示**
- **字符串函数**：`Len`, `Left`, `Right`, `Mid`, `UCase`, `LCase`, `Trim`, `Replace`, `InStr`, `Split`, `Join` 等
- **数值函数**：`Abs`, `Int`, `Fix`, `Round`, `Sqr`, `Sin`, `Cos`, `Tan`, `Log`, `Exp`, `Rnd` 等
- **类型转换函数**：`CStr`, `CInt`, `CDbl`, `CBool`, `CDate`, `CCur` 等
- **日期时间函数**：`Now`, `Date`, `Time`, `Year`, `Month`, `Day`, `Hour`, `Minute`, `Second` 等
- **判断函数**：`IsNull`, `IsEmpty`, `IsNumeric`, `IsDate`, `IsArray`, `IsObject` 等
- **对话框函数**：`MsgBox`, `InputBox` 等

**3. 内置对象提示**
- **Request 对象**：`Request.Form`, `Request.QueryString`, `Request.Cookies`, `Request.ServerVariables` 等
- **Response 对象**：`Response.Write`, `Response.Redirect`, `Response.End`, `Response.Buffer` 等
- **Server 对象**：`Server.CreateObject`, `Server.MapPath`, `Server.HTMLEncode`, `Server.URLEncode` 等
- **Session 对象**：`Session.Abandon`, `Session.Timeout`, `Session.SessionID` 等
- **Application 对象**：`Application.Lock`, `Application.UnLock` 等
- **Err 对象**：`Err.Number`, `Err.Description`, `Err.Clear`, `Err.Raise` 等

**4. 智能提示特性**
- **分类显示**：提示按功能分类，便于快速找到所需的关键词
- **自动空格**：插入关键词后自动添加适当的空格，提高编码效率
- **大小写规范**：所有提示的关键词都使用标准的 Pascal 大小写格式
- **详细描述**：每个提示都包含功能描述，帮助理解关键词的用途

#### 调试功能
- **键盘事件调试**：按 `Ctrl + Alt + D` 记录当前编辑状态
- **详细日志输出**：记录项目信息、文件信息、光标位置、选中文本等
- **控制台调试**：所有调试信息输出到IDE控制台
- **实时状态监控**：可以随时查看当前编辑器的详细状态

#### 支持的文件类型
- `.asp` 文件：Classic ASP 文件，支持 ASP 和 HTML 混合语法

### 开发环境设置

#### 系统要求
- Java 17 或更高版本
- Gradle 7.0 或更高版本
- IntelliJ IDEA 或 PyCharm（用于开发）

#### 配置运行配置

1. 在 PyCharm 中，点击右上角的 "Run Configurations"
2. 点击 "+" 按钮，选择 "Gradle"
3. 配置以下设置：
   - Name: `Run Plugin`
   - Gradle project: 选择项目根目录
   - Tasks: `runIde`
4. 点击 "Apply" 和 "OK"

#### 构建和运行插件

1. **构建插件**：
   ```bash
   ./gradlew build
   ```

2. **运行插件进行测试**：
   ```bash
   ./gradlew runIde
   ```

3. **构建插件包**：
   ```bash
   ./gradlew buildPlugin
   ```

### 贡献

欢迎提交 Issue 和 Pull Request 来改进这个插件。

### 许可证

本项目采用 MIT 许可证。
