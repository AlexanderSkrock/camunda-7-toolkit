/*
  ------------------------------------------------------------------------------
        (c) by data experts gmbh
              Postfach 1130
              Woldegker Str. 12
              17001 Neubrandenburg

  Dieses Dokument und die hierin enthaltenen Informationen unterliegen
  dem Urheberrecht und duerfen ohne die schriftliche Genehmigung des
  Herausgebers weder als ganzes noch in Teilen dupliziert oder reproduziert
  noch manipuliert werden.
*/

package dev.skrock.camunda.toolkit.ui.analyze.variables;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import lombok.Getter;
import org.springframework.util.unit.DataSize;

/**
 * <Beschreibung>
 * <br>
 *
 * @author askrock
 */
public class AnalyzeVariablesArgumentsEditor extends FormLayout {

    @Getter
    private Binder<AnalyzeVariablesArguments> binder = new BeanValidationBinder<>(AnalyzeVariablesArguments.class);

    public AnalyzeVariablesArgumentsEditor() {
        TextField processInstanceIdField = new TextField();
        addFormItem(processInstanceIdField, "ProcessInstanceId");

        TextField minDataSize = new TextField();
        minDataSize.setPattern("\\d+( )?[KMG]?B");
        addFormItem(minDataSize, "Min Size");

        binder.bind(processInstanceIdField, AnalyzeVariablesArguments::getProcessInstanceId, AnalyzeVariablesArguments::setProcessInstanceId);
        binder.bind(minDataSize, o -> o.getMinSize().toString(), (o, s) -> o.setMinSize(DataSize.parse(s)));

        binder.readBean(new AnalyzeVariablesArguments());
    }
}
