package org.inftel.ssa.web;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import org.inftel.ssa.domain.Task;

public class TaskConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Task task = new Task();
		task.setId(Long.parseLong(value));
		return task;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		return ((Task) value).getId().toString();
	}
}
