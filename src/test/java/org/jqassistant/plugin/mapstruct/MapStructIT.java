package org.jqassistant.plugin.mapstruct;

import java.util.List;
import java.util.Map;

import com.buschmais.jqassistant.core.report.api.model.Result;
import com.buschmais.jqassistant.core.rule.api.model.Concept;
import com.buschmais.jqassistant.core.rule.api.model.RuleException;
import com.buschmais.jqassistant.plugin.java.api.model.TypeDescriptor;
import com.buschmais.jqassistant.plugin.java.test.AbstractJavaPluginIT;

import org.jqassistant.plugin.mapstruct.types.MapperClass;
import org.jqassistant.plugin.mapstruct.types.MapperClassImpl;
import org.jqassistant.plugin.mapstruct.types.MapperInterface;
import org.jqassistant.plugin.mapstruct.types.MapperInterfaceImpl;
import org.junit.jupiter.api.Test;

import static com.buschmais.jqassistant.core.report.api.model.Result.Status.SUCCESS;
import static com.buschmais.jqassistant.plugin.java.test.matcher.TypeDescriptorMatcher.typeDescriptor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.HamcrestCondition.matching;
import static org.hamcrest.Matchers.hasItems;

public class MapStructIT extends AbstractJavaPluginIT {

    @Test
    void mapper() throws RuleException {
        scanClasses(MapperInterface.class, MapperInterfaceImpl.class, MapperClass.class, MapperClassImpl.class);

        Result<Concept> result = applyConcept("mapstruct:Mapper");

        assertThat(result.getStatus()).isEqualTo(SUCCESS);
        store.beginTransaction();
        List<Map<String, Object>> rows = result.getRows();
        assertThat(rows).hasSize(2);
        List<TypeDescriptor> mappers = query("MATCH (mapper:MapStruct:Mapper) RETURN mapper").getColumn("mapper");
        assertThat(mappers).has(matching(hasItems(typeDescriptor(MapperInterface.class), typeDescriptor(MapperClass.class))));
        store.commitTransaction();
    }

    @Test
    void generatedType() throws RuleException {
        scanClasses(MapperInterface.class, MapperInterfaceImpl.class, MapperClass.class, MapperClassImpl.class);

        Result<Concept> result = applyConcept("java:GeneratedType"); // implicitly executes "mapstruct:GeneratedType"

        assertThat(result.getStatus()).isEqualTo(SUCCESS);
        store.beginTransaction();
        List<TypeDescriptor> generatedTypes = query("MATCH (generatedType:MapStruct:Generated) RETURN generatedType").getColumn("generatedType");
        assertThat(generatedTypes).has(matching(hasItems(typeDescriptor(MapperInterfaceImpl.class), typeDescriptor(MapperClassImpl.class))));
        store.commitTransaction();
    }
}
