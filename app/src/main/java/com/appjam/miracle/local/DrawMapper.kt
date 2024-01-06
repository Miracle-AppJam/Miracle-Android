package com.appjam.miracle.local


internal fun DrawEntity.toModel() =
    DrawModel(
        id = id,
        image = image,
        message = message
    )

internal fun List<DrawEntity>.toModels() =
    this.map {
        it.toModel()
    }