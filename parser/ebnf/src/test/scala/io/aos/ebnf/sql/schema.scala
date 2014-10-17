package io.aos.ebnf.sql

import io.aos.ebnf.sql.DateType;
import io.aos.ebnf.sql.Definitions;

object TestSchema {
  val definition = new Definitions(Map("nation" -> Seq(TableColumn("n_comment", VariableLenString(152)), TableColumn("n_regionkey", IntType(4)), TableColumn("n_name", FixedLenString(25)), TableColumn("n_nationkey", IntType(4))), "partsupp" -> Seq(TableColumn("ps_comment", VariableLenString(199)), TableColumn("ps_supplycost", DecimalType(15,2)), TableColumn("ps_availqty", IntType(4)), TableColumn("ps_suppkey", IntType(4)), TableColumn("ps_partkey", IntType(4))), "supplier" -> Seq(TableColumn("s_comment", VariableLenString(101)), TableColumn("s_acctbal", DecimalType(15,2)), TableColumn("s_phone", FixedLenString(15)), TableColumn("s_nationkey", IntType(4)), TableColumn("s_address", VariableLenString(40)), TableColumn("s_name", FixedLenString(25)), TableColumn("s_suppkey", IntType(4))), "customer" -> Seq(TableColumn("c_comment", VariableLenString(117)), TableColumn("c_mktsegment", FixedLenString(10)), TableColumn("c_acctbal", DecimalType(15,2)), TableColumn("c_phone", FixedLenString(15)), TableColumn("c_nationkey", IntType(4)), TableColumn("c_address", VariableLenString(40)), TableColumn("c_name", VariableLenString(25)), TableColumn("c_custkey", IntType(4))), "region" -> Seq(TableColumn("r_comment", VariableLenString(152)), TableColumn("r_name", FixedLenString(25)), TableColumn("r_regionkey", IntType(4))), "orders" -> Seq(TableColumn("o_comment", VariableLenString(79)), TableColumn("o_shippriority", IntType(4)), TableColumn("o_clerk", FixedLenString(15)), TableColumn("o_orderpriority", FixedLenString(15)), TableColumn("o_orderdate", DateType), TableColumn("o_totalprice", DecimalType(15,2)), TableColumn("o_orderstatus", FixedLenString(1)), TableColumn("o_custkey", IntType(4)), TableColumn("o_orderkey", IntType(4))), "lineitem" -> Seq(TableColumn("l_comment", VariableLenString(44)), TableColumn("l_shipmode", FixedLenString(10)), TableColumn("l_shipinstruct", FixedLenString(25)), TableColumn("l_receiptdate", DateType), TableColumn("l_commitdate", DateType), TableColumn("l_shipdate", DateType), TableColumn("l_linestatus", FixedLenString(1)), TableColumn("l_returnflag", FixedLenString(1)), TableColumn("l_tax", DecimalType(15,2)), TableColumn("l_discount", DecimalType(15,2)), TableColumn("l_extendedprice", DecimalType(15,2)), TableColumn("l_quantity", DecimalType(15,2)), TableColumn("l_linenumber", IntType(4)), TableColumn("l_suppkey", IntType(4)), TableColumn("l_partkey", IntType(4)), TableColumn("l_orderkey", IntType(4))), "part" -> Seq(TableColumn("p_comment", VariableLenString(23)), TableColumn("p_retailprice", DecimalType(15,2)), TableColumn("p_container", FixedLenString(10)), TableColumn("p_size", IntType(4)), TableColumn("p_type", VariableLenString(25)), TableColumn("p_brand", FixedLenString(10)), TableColumn("p_mfgr", FixedLenString(25)), TableColumn("p_name", VariableLenString(55)), TableColumn("p_partkey", IntType(4)))))
}