# Classic ASP 统一处理器改进

## 概述

本次改进将语法高亮和语法验证逻辑合并成一个统一的处理器，提高了性能和可维护性。

## 主要改进

### 1. 统一处理器架构

创建了 `ClassicASPLanguageProcessor` 类，整合了以下功能：
- **统一的词法分析**：避免重复解析
- **共享的语义分析**：复用分析结果
- **统一的高亮生成**：基于词法分析结果
- **统一的验证错误**：基于语义分析结果

### 2. 性能优化

#### 缓存机制
- **结果缓存**：30秒缓存超时
- **文件级缓存**：基于文件路径和内容哈希
- **自动失效**：内容变化时自动清除缓存

#### 减少重复解析
- **一次词法分析**：同时为高亮和验证提供数据
- **共享语义分析**：避免重复的代码块分析
- **统一Token处理**：减少内存使用

### 3. 代码结构优化

#### 新的类结构
```
ClassicASPLanguageProcessor
├── ProcessingResult (处理结果)
├── TokenInfo (Token信息)
├── HighlightInfo (高亮信息)
└── ValidationError (验证错误)
```

#### 简化的组件
- **UnifiedClassicASPSyntaxHighlighter**：统一的语法高亮器
- **ClassicASPSyntaxValidator**：简化的语法验证器
- **ClassicASPSyntaxHighlighterFactory**：更新的工厂类

### 4. 功能特性

#### 语法高亮
- ASP 标签高亮
- 关键字高亮
- 内置对象高亮
- 标识符高亮
- 注释高亮

#### 语法验证
- ASP 标签匹配检查
- 重复标签检查
- 语义错误检查

#### 性能监控
- 缓存统计信息
- 处理时间监控
- 内存使用优化

## 使用方式

### 1. 直接使用统一处理器

```java
// 处理文件内容
String content = file.getText();
String filePath = file.getVirtualFile().getPath();
ClassicASPLanguageProcessor.ProcessingResult result =
    ClassicASPLanguageProcessor.process(content, filePath);

// 获取高亮信息
List<HighlightInfo> highlights = result.getHighlights();

// 获取验证错误
List<ValidationError> errors = result.getErrors();
```

### 2. 通过 Annotator 接口

```java
// 在 Annotator 中使用
ClassicASPLanguageProcessor.processForAnnotator(element, holder);
```

### 3. 缓存管理

```java
// 清除特定文件缓存
ClassicASPLanguageProcessor.invalidateCache(filePath);

// 清除所有缓存
ClassicASPLanguageProcessor.clearCache();
```

## 性能对比

### 改进前
- 语法高亮：独立词法分析
- 语法验证：独立词法分析
- 重复解析：2次词法分析
- 内存使用：双倍Token存储

### 改进后
- 统一处理：1次词法分析
- 共享结果：复用分析数据
- 缓存机制：减少重复计算
- 内存优化：单份Token存储

## 测试验证

### 测试文件
- `test/unified_processor_test.asp`：完整的测试用例
- 包含各种语法结构
- 测试高亮和验证功能

### 验证要点
1. **语法高亮正确性**：确保所有Token正确高亮
2. **语法验证准确性**：确保错误检测准确
3. **性能提升**：验证处理速度提升
4. **缓存效果**：验证缓存机制工作正常

## 向后兼容性

### 保持的接口
- `ClassicASPSyntaxHighlighterFactory`：工厂接口不变
- `ClassicASPSyntaxValidator`：验证器接口不变
- `plugin.xml`：配置保持不变

### 新增功能
- `ClassicASPLanguageProcessor`：统一处理器
- `UnifiedClassicASPSyntaxHighlighter`：统一高亮器
- 缓存管理功能

## 未来扩展

### 1. 更多验证规则
- 变量作用域检查
- 函数调用验证
- 类型检查

### 2. 更智能的缓存
- 增量更新
- 智能失效
- 内存优化

### 3. 性能监控
- 处理时间统计
- 内存使用监控
- 缓存命中率

## 总结

通过统一处理器的改进，我们实现了：

1. **性能提升**：减少50%的重复解析
2. **代码简化**：统一的处理逻辑
3. **缓存优化**：智能的缓存机制
4. **可维护性**：清晰的代码结构
5. **扩展性**：易于添加新功能

这个改进为插件的长期发展奠定了良好的基础。
