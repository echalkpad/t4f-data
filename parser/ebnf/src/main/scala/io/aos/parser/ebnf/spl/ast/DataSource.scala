package io.aos.parser.ebnf.spl.ast

import io.aos.parser.ebnf.spl.backend.BackendType

case class DataSource(src: String, backend: BackendType.Value = BackendType.ElasticSearch)
