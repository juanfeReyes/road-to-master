import { ProductLog } from "@/domain/ProductLog"
import React, { ComponentType} from "react"
import { ProductLogItemListProps } from "../Product/ProductLogItemList"

interface ListGridProps {
  data: ProductLog[],
  Item: ComponentType<ProductLogItemListProps>
  headers?: string[]
}

export const ListGrid = ({ data, Item, headers }: ListGridProps) => {

  return (
    <div>
      <div className="flex gap-2 pb-2 bg-slate-100">
        {
          headers && headers.map((header, idx) => <div key={`list-grid-header-${idx}`} className="w-1/3">{header}</div>)
        }
      </div>
      {
        data.map((item, idx) =>
          <Item key={idx} data={item}/>
        )
      }
    </div>
  )
}
