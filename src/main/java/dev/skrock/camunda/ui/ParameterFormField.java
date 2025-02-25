package dev.skrock.camunda.ui;

//import io.swagger.v3.oas.annotations.Parameter;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;

import java.lang.reflect.Parameter;
import java.util.Optional;

public class ParameterFormField {

    public static Component forParameter(Parameter param, HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<?, ?>> valueHandler) {
        if (String.class.isAssignableFrom(param.getType())) {
            return forStringParameter(param, valueHandler::valueChanged);
        } else if (Number.class.isAssignableFrom(param.getType())) {
            return forNumberParameter(param, valueHandler::valueChanged);
        } else {
            // TODO support additional types
            return forUnsupportedParameter(param, valueHandler::valueChanged);
        }
    }

    public static TextField forStringParameter(Parameter parameter, HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<TextField, String>> valueHandler) {
        // TODO respect required parameters
        return new TextField(getLabel(parameter), valueHandler);
    }

    public static NumberField forNumberParameter(Parameter parameter, HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<NumberField, Double>> valueHandler) {
        // TODO respect required parameters
        return new NumberField(getLabel(parameter), valueHandler);
    }

    public static Component forUnsupportedParameter(Parameter parameter, HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<NumberField, Double>> valueHandler) {
        return new TextField(getLabel(parameter), "unsupported type " + parameter.getType());
    }

    public static String getLabel(Parameter parameter) {
        return Optional.ofNullable(parameter.getAnnotation(io.swagger.v3.oas.annotations.Parameter.class))
                .map(io.swagger.v3.oas.annotations.Parameter::name)
                .orElse(parameter.getName());
    }
}
