import { ProductLog } from "@/domain/ProductLog"
import React, { ComponentType} from "react"
import { ProductLogItemListProps } from "../Product/ProductLogItemList"

interface ListGridProps {
  data: ProductLog[],
  Item: ComponentType<ProductLogItemListProps>
}

export const ListGrid = ({ data, Item }: ListGridProps) => {

  return (
    <div>
      {
        data.map((item, idx) =>
          <Item key={idx} data={item}/>
        )
      }
    </div>
  )
}
