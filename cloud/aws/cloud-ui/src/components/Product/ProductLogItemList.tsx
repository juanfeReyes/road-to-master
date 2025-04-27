import React from "react"
import { ProductLog } from "@/domain/ProductLog"

export interface ProductLogItemListProps {
  data: ProductLog
}

export const ProductLogItemList = ({data}: ProductLogItemListProps) => {

  return (
    <div key={data.id} className="flex gap-2 ">
      <div className="w-1/3">{data.objectKey}</div>
      <div className="w-1/3">{data.createTime}</div>
      <div className="w-1/3">{data.tags}</div>
    </div>
  ) 
}

