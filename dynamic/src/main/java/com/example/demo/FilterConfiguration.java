package com.example.demo;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:filterproperties.properties")
public class FilterConfiguration {

//	@Bean
//	public FilterRegistrationBean simpleFilter() {
//		
//		FilterRegistrationBean registration = new FilterRegistrationBean();
//        Filter myFilter = new SimpleFilter();
//        
//        registration.setFilter(myFilter);
//        registration.addUrlPatterns("/*");
//        return registration;
//	}
	
	@Bean
	public static BeanDefinitionRegistryPostProcessor factory(Environment environment) {
		
		final List<FilterData> filters = filterMap(environment);
		
		return new BeanDefinitionRegistryPostProcessor() {
			
			@Override
			public void postProcessBeanFactory(ConfigurableListableBeanFactory factory) throws BeansException {
				
				filters.stream()
					.map(FilterConfiguration::from)
					.forEach(filter -> factory.registerSingleton(filter.getName(), filter));
			}
			
			@Override
			public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry arg0) throws BeansException {
			}
		};
	}
	
	private static NamedFilterRegistrationBean from(FilterData filterData) {
		
		NamedFilterRegistrationBean registration = new NamedFilterRegistrationBean();
        
		try {
		
			Class<? extends Filter> filterClass = Class.forName(filterData.clazzName).asSubclass(Filter.class);
	        
	        Filter filter = filterClass.newInstance();
	        registration.setFilter(filter);
	        registration.addUrlPatterns("/*");
	        registration.setOrder(filterData.order);
	        registration.setName(filterData.getName());
        
		} catch (Exception e) {
			throw new RuntimeException("Unable to configure create filter registration bean", e);
		}
        
        return registration;
		
	}
	
	private static class NamedFilterRegistrationBean extends FilterRegistrationBean {
		
		public String getName() {
			
			return getOrDeduceName("unknown");
		}
		
	}
	
//	private static List<Address> addressMap(Environment environment) {
//		
//		int index = 0;
//		List<Address> addresses = new ArrayList<>();
//		while (environment.containsProperty(String.format("stuff.addresses[%s].address.line1", index))) {
//			Address address = new Address();
//			address.setLine1(environment.getProperty(String.format("stuff.addresses[%s].address.line1", index)));
//			address.setPostCode(environment.getProperty(String.format("stuff.addresses[%s].address.postCode", index)));
//			addresses.add(address);
//			index = index + 1;
//		}
//		
//		
//		return addresses;
//	}

	private static List<FilterData> filterMap(Environment environment) {
		
		int index = 0;
		List<FilterData> filters = new ArrayList<>();
		while (environment.containsProperty(String.format("filterconfig.filters[%s].order", index))) {
			FilterData filter = new FilterData();
			filter.setOrder(environment.getProperty(String.format("filterconfig.filters[%s].order", index), Integer.class));
			filter.setName(environment.getProperty(String.format("filterconfig.filters[%s].name", index)));
			filter.setClazzName(environment.getProperty(String.format("filterconfig.filters[%s].clazzName", index)));
			filter.setUrlPatterns(environment.getProperty(String.format("filterconfig.filters[%s].urlPatterns", index)));
			filters.add(filter);
			index = index + 1;
		}
		
		
		return filters;
	}

	public static class FilterData {
		
		private String name;
		private String clazzName;
		private int order;
		private String urlPatterns;
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getOrder() {
			return order;
		}
		public void setOrder(int order) {
			this.order = order;
		}
		public String getClazzName() {
			return clazzName;
		}
		public void setClazzName(String clazzName) {
			this.clazzName = clazzName;
		}
		public String getUrlPatterns() {
			return urlPatterns;
		}
		public void setUrlPatterns(String urlPatterns) {
			this.urlPatterns = urlPatterns;
		}
		
	}	
	
}
