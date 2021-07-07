package taskflow.script.test

/**
 * 模板引擎
 * */
///简单模板引擎SimpleTemplateEngine
def text ='This Tutorial focuses on $TutorialName. In this tutorial you will learn about $Topic'
//也可以是外部文件def file = new File("D:/Student.template") 使用和text一样
def binding = ["TutorialName":"Groovy", "Topic":"SimpleTemplates"]
def engine = new groovy.text.SimpleTemplateEngine()
def template = engine.createTemplate(text).make(binding)
println template
//插值字符串其实就是简单模板
def name = "Groovy"
println "This Tutorial is about ${name}"

///流模板引擎StreamingTemplateEngine
///使用可写的闭包创建模板，使其对于大模板更具可扩展性。特别是这个模板引擎可以处理大于64k的字符串
text = '''This Tutorial is <% out.print 'xxx'+TutorialName %> The Topic name
is $TopicName''' 
template = new groovy.text.StreamingTemplateEngine().createTemplate(text)
binding = [TutorialName : "Groovy", TopicName  : "StreamingTemplates",]
String response = template.make(binding)
println(response)

///XML模板引擎XMLTemplateEngine
///模板源和预期输出都是XML，模板使用正常的$ {expression}和$ variable表示法将任意表达式插入到模板中
binding = [StudentName: 'Joe', id: 1, subject: 'Physics']
engine = new groovy.text.XmlTemplateEngine()
text = '''
   <document xmlns:gsp='http://groovy.codehaus.org/2005/gsp'>
      <Student>
         <name>${StudentName}</name>
         <ID>${id}</ID>
         <subject>${subject}</subject>
      </Student>
   </document> 
''' 
template = engine.createTemplate(text).make(binding)
println template.toString()