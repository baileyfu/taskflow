package taskflow.config.bean;

/**
 * TaskBean类型定义</p>
 * beanId唯一
 */
public class TaskBeanDefinition {
	private String beanId;
	private Class<?> beanClazz;
	public String getBeanId() {
		return beanId;
	}
	public void setBeanId(String beanId) {
		this.beanId = beanId;
	}
	public Class<?> getBeanClazz() {
		return beanClazz;
	}
	public void setBeanClazz(Class<?> beanClazz) {
		this.beanClazz = beanClazz;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((beanId == null) ? 0 : beanId.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TaskBeanDefinition other = (TaskBeanDefinition) obj;
		if (beanId == null) {
			if (other.beanId != null)
				return false;
		} else if (!beanId.equals(other.beanId))
			return false;
		return true;
	}
}
