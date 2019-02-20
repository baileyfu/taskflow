package taskflow.script.test

/** ==等于equals */
println "aaa"=="Kaiser"

/** 默认访问修饰符public */
def age=1
assert  age instanceof Integer
/** 默认使用的类型为 BigDecimal */
def decimal = 123.456
println decimal.getClass()

/** 类型后缀来申明参数类型 */
def year=2019I
def money=123.456G
println "${year.getClass()}---${money.getClass()}"

/** 字符串 */
def string="string"
//普通字符串
println 'Normal String ${string}'
//插值字符串
println "Value String ${string}"

/** 文本的换行及缩进格式 */
def strippedNewline='''\
		line1
			line2
				line3'''
println strippedNewline

/** Groovy 中并没有明确的字符字面量表示形式，需要显示的指定 */
char c1 = 'A' // 声明类型
assert c1 instanceof Character
def c2 = 'B' as char // 用as关键字
assert c2 instanceof Character
def c3 = (char) 'C' // 强制类型转换
assert c3 instanceof Character

/***
 * 正则
 * 使用〜'regex'表达式本地支持正则表达式
 */
def regex = ~'[a-z]*'
println regex.matcher('Groovy').matches()
//查找运算符
def x = '13090987890' =~ '1[0-9]{10}'
println x.matches()
//匹配运算符
println 'Groovy' ==~ 'Groovy'