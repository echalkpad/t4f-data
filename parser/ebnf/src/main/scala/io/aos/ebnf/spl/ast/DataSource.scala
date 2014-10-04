package io.aos.ebnf.spl.ast

import io.aos.ebnf.spl.backend.BackendType

case class DataSource(src: String, backend: BackendType.Value = BackendType.ElasticSearch)
