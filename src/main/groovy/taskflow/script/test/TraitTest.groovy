package taskflow.script.test

/**
 * 特征
 * 特征是语言的结构构造，可以被看作是承载默认实现和状态的接口
 * */
trait Marks{
	String name
	void showMark() {
		println "Mark:${name}"
	}
}
trait Man extends Marks{
	String kind
	void showKind() {
		println "Kind:${kind}"
	}
}
class AAA{
	int age
}
class Student extends AAA implements Man{
	int id
	void showId() {
		println "Id:${id}"
	}
	@Override
	public String toString() {
		return com.alibaba.fastjson.JSON.toJSONString(this);
	}
}
Student s=new Student();
s.name='kaiser'
s.kind='ManKind'
s.id=10
s.age=100
s.showKind()
s.showMark()
println s
println s.getClass().getSuperclass()