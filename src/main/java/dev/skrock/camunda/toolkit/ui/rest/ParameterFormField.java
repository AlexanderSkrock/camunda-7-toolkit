package dev.skrock.camunda.toolkit.ui.rest;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;

public class ParameterFormField {

    public static Component forParameter(RestApiMethodParameter param, HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<?, ?>> valueHandler) {
        if (String.class.isAssignableFrom(param.type())) {
            return forStringParameter(param, valueHandler::valueChanged);
        } else if (Number.class.isAssignableFrom(param.type())) {
            return forNumberParameter(param, valueHandler::valueChanged);
        } else {
            // TODO support additional types
            return forUnsupportedParameter(param, valueHandler::valueChanged);
        }
    }

    public static TextField forStringParameter(RestApiMethodParameter parameter, HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<TextField, String>> valueHandler) {
        // TODO respect required parameters
        return new TextField(null, valueHandler);
    }

    public static NumberField forNumberParameter(RestApiMethodParameter parameter, HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<NumberField, Double>> valueHandler) {
        // TODO respect required parameters
        return new NumberField(null, valueHandler);
    }

    public static Component forUnsupportedParameter(RestApiMethodParameter parameter, HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<NumberField, Double>> valueHandler) {
        return new TextField(null, "unsupported type " + parameter.type());
    }
}
