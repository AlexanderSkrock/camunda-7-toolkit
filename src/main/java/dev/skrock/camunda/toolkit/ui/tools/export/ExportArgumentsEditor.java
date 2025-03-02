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

package dev.skrock.camunda.toolkit.ui.tools.export;

import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import lombok.Getter;

/**
 * <Beschreibung>
 * <br>
 *
 * @author askrock
 */
public class ExportArgumentsEditor extends FormLayout {

    @Getter
    private Binder<ExportArguments> binder = new BeanValidationBinder<>(ExportArguments.class);

    public ExportArgumentsEditor() {
        CheckboxGroup<Includes> includesField = new CheckboxGroup<>();
        includesField.setItems(Includes.values());
        includesField.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);
        addFormItem(includesField, "Types");

        binder.bind(includesField, ExportArguments::getIncludes, ExportArguments::setIncludes);
    }
}
