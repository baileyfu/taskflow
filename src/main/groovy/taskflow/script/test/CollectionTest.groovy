package taskflow.script.test

/** List 
 *  使用中括号([])，以逗号(,)分隔元素即可
 *  Groovy中的 List 其实就是java.util.List，实现类默认使用的是java.util.ArrayList
 * */
def list=[1,2,3,'4']
println ''+list.getClass()+'-'+list.size()

/**
 * Array
 * 数组的定义必须指定类型为数组类型，可以直接定义类型或者使用def定义然后通过as关键字来指定其类型
 */
def arr1=[1,2,3] as int[]
int[] arr2=['A','B','C']
println ''+arr1.getClass()+'-'+arr1.length
println ''+arr2.getClass()+'-'+arr2.length

/**
 * Map
 * 中括号包含key、val的形式，key和value以冒号分隔([key:value])
 * Groovy中的Map其实就是java.util.Map，实现类默认使用的是java.util.LinkedHashMap
 */
def v='orange'
//要使用变量作为key，需要使用括号
def colors = [red: '#FF0000', green: '#00FF00', 'blue': '#0000FF',v: '#XXXOOO',(v):'#VVVYYY']
//put
colors['pink'] = '#FF00FF'
colors.yellow = '#FFFF00'
colors.put('black','#FFFFFF')
def val = 'white'
colors[val]='#000000'
println colors['val']+'-'+colors[val]+'-'+colors['white']

println ''+colors.getClass()+'-'+colors.size()
println colors.get('red')+'-'+colors['blue']+'-'+colors.green
println colors

/**
 * Rang
 * 使用..操作符来定义一个区间对象，简化范围操作的代码
 * 实际上是List接口的实现
 * */
def rang1 = 10..1
def rang2 = 'a'..<'d' //当于左闭右开区间
println ''+rang1.getClass()+'-'+rang2.getClass()
println rang1.collect()+'---'+rang2.collect()
rang2.each { println it }
0.upto(5) { print "$it " }//确定下限和上限
println ''
0.step(10,2){ print "$it " }//确定下限和上限并指定步长
println ''
5.times{ print "$it " }//从0开始迭代
println ''
def age=25
switch(age) {
	case(0..3):
		println '婴幼儿'
		break
	case(4..16):
		println '小孩'
		break
	case(17..<26):
		println '青年'
		break
}

