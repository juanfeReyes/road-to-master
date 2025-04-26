import React from "react"
import { ProductLog } from "@/domain/ProductLog"

export interface ProductLogItemListProps {
  data: ProductLog
}

export const ProductLogItemList = ({data}: ProductLogItemListProps) => {

  return (
    <div key={data.id} className="flex gap-2">
      <div>{data.content}</div>
      <div>{data.createTime}</div>
    </div>
  ) 
}

