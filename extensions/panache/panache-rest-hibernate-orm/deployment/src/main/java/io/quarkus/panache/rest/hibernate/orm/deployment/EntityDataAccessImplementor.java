package io.quarkus.panache.rest.hibernate.orm.deployment;

import static io.quarkus.gizmo.MethodDescriptor.ofMethod;

import java.util.List;

import javax.persistence.EntityManager;

import io.quarkus.gizmo.BytecodeCreator;
import io.quarkus.gizmo.ResultHandle;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.runtime.JpaOperations;
import io.quarkus.panache.rest.common.deployment.DataAccessImplementor;

final class EntityDataAccessImplementor implements DataAccessImplementor {

    private final String entityClassName;

    EntityDataAccessImplementor(String entityClassName) {
        this.entityClassName = entityClassName;
    }

    @Override
    public ResultHandle findById(BytecodeCreator creator, ResultHandle id) {
        return creator.invokeStaticMethod(ofMethod(entityClassName, "findById", PanacheEntityBase.class, Object.class), id);
    }

    @Override
    public ResultHandle listAll(BytecodeCreator creator) {
        return creator.invokeStaticMethod(ofMethod(entityClassName, "listAll", List.class));
    }

    @Override
    public ResultHandle persist(BytecodeCreator creator, ResultHandle entity) {
        creator.invokeVirtualMethod(ofMethod(entityClassName, "persist", void.class), entity);
        return entity;
    }

    @Override
    public ResultHandle update(BytecodeCreator creator, ResultHandle entity) {
        ResultHandle entityManager = creator.invokeStaticMethod(
                ofMethod(JpaOperations.class, "getEntityManager", EntityManager.class));
        return creator.invokeInterfaceMethod(
                ofMethod(EntityManager.class, "merge", Object.class, Object.class), entityManager, entity);
    }

    @Override
    public ResultHandle deleteById(BytecodeCreator creator, ResultHandle id) {
        return creator.invokeStaticMethod(ofMethod(entityClassName, "deleteById", boolean.class, Object.class), id);
    }
}
