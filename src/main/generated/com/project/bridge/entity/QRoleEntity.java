package com.project.bridge.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QRoleEntity is a Querydsl query type for RoleEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRoleEntity extends EntityPathBase<RoleEntity> {

    private static final long serialVersionUID = -875850783L;

    public static final QRoleEntity roleEntity = new QRoleEntity("roleEntity");

    public final NumberPath<Long> idx = createNumber("idx", Long.class);

    public final StringPath role = createString("role");

    public final NumberPath<Long> userIdx = createNumber("userIdx", Long.class);

    public QRoleEntity(String variable) {
        super(RoleEntity.class, forVariable(variable));
    }

    public QRoleEntity(Path<? extends RoleEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRoleEntity(PathMetadata metadata) {
        super(RoleEntity.class, metadata);
    }

}

