package taskflow.script.test

def name="Kaiser";
/** 闭包可以使用外部变量 */
//多个参数以逗号分隔，参数类型和方法一样可以显式声明也可省略
def closure = { p1,int p2 -> println "${name} say : hello ${p1} , i'm ${p2}" }
closure.call('world',1)
closure('A',2)
closure'B',3
println closure.metaPropertyValues
closure = {'没有参数的话可以省略->操作符'}
println closure.call()
closure = {"只有一个参数的话，也可省略参数的定义，Groovy提供了一个隐式的参数it来替代它: ${it}"}
println closure(1)

//闭包可以作为参数传入
def method(name,c) {
	c.call(name);
}
//闭包作为方法的唯一参数或最后一个参数时可省略括号
println method('Kaiser') {"One parameter : ${it}"}

