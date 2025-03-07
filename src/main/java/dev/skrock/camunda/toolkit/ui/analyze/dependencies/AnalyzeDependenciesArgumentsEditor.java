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

package dev.skrock.camunda.toolkit.ui.analyze.dependencies;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import lombok.Getter;

/**
 * <Beschreibung>
 * <br>
 *
 * @author askrock
 */
public class AnalyzeDependenciesArgumentsEditor extends FormLayout {

    @Getter
    private Binder<AnalyzeDependenciesArguments> binder = new BeanValidationBinder<>(AnalyzeDependenciesArguments.class);

    public AnalyzeDependenciesArgumentsEditor() {
        TextField processDefinitionIdField = new TextField();
        addFormItem(processDefinitionIdField, "Process Definition ID");

        Checkbox includeTransitiveField = new Checkbox();
        // TODO implemented deep search
        includeTransitiveField.setReadOnly(true);
        addFormItem(includeTransitiveField, "Transitive?");

        binder.bind(processDefinitionIdField, AnalyzeDependenciesArguments::getProcessDefinitionId, AnalyzeDependenciesArguments::setProcessDefinitionId);
        binder.bind(includeTransitiveField, AnalyzeDependenciesArguments::isIncludeTransitive, AnalyzeDependenciesArguments::setIncludeTransitive);

        binder.readBean(new AnalyzeDependenciesArguments());
    }
}
