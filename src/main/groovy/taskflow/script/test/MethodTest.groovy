package taskflow.script.test

/** 默认访问修饰符public 
 *  支持重载、不定长参数等java特性
 * */

//方法的返回类型可以不需要声明，但需添加def关键字
def sayHello() {
	//默认返回最后一行代码的运行结果
	'sayHello'
}
//方法的参数类型可以被省略，默认为Object类型
void noReturn(i,j){
	println 'Kaiser '+sayHello()+' i: '+i+' , j: '+j
}
String returnDefined(int x) {
	//使用return关键字则返回指定的返回值
	return "returnDefined , parameter : ${1000/x}"
}

String result=sayHello()
println result
noReturn 'i',888
try {
	result = returnDefined 3
	println result
}catch(Exception e) {
	e.printStackTrace()
}

/**
 * DSL
 */
class EmailDSL{
	String toText
	String fromText
	String body
	
	def body(body) {
		this.body=body
	}
	def from(fromText) {
		this.fromText=fromText
	}
	def to(toText) {
		this.toText=toText
	}
	String toString() {
		return "EmailDSL [toText=" + toText + ", fromText=" + fromText + ", body=" + body + "]";
	}
	static def makeEmail(Closure  closure) {
		EmailDSL eDSL=new EmailDSL()
		closure.delegate =eDSL
		closure.call()
		return eDSL
	}
}
def email=EmailDSL.makeEmail{
	delegate.from '来自XXX'
	to '发送给YYY'
	body '内容ZZZ'
}
println email