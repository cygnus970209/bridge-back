package com.project.bridge.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMailAuthEntity is a Querydsl query type for MailAuthEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMailAuthEntity extends EntityPathBase<MailAuthEntity> {

    private static final long serialVersionUID = 1343486122L;

    public static final QMailAuthEntity mailAuthEntity = new QMailAuthEntity("mailAuthEntity");

    public final NumberPath<Integer> code = createNumber("code", Integer.class);

    public final StringPath email = createString("email");

    public final NumberPath<Long> idx = createNumber("idx", Long.class);

    public QMailAuthEntity(String variable) {
        super(MailAuthEntity.class, forVariable(variable));
    }

    public QMailAuthEntity(Path<? extends MailAuthEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMailAuthEntity(PathMetadata metadata) {
        super(MailAuthEntity.class, metadata);
    }

}

