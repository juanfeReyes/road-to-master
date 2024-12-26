'use client'

import { DialogCloud } from "@/components/Dialog/DialogCloud";
import { ListGrid } from "@/components/ListGrid/ListGrid";
import { ProductLogItemList } from "@/components/Product/ProductLogItemList";
import { ProductLog, ProductLogRequest } from "@/domain/ProductLog";
import { createProductLog, getProductLogs } from "@/services/productLogService";
import { ChangeEvent, useEffect, useState } from "react";

export default function Home() {

  const [productLogs, setProductLogs] = useState<ProductLog[]>([])
  const [logContent, setLogContent] = useState("")

  useEffect(() => {
    async function fetchLogs() {
      const logs = Object.values(await getProductLogs());
      setProductLogs(logs);
    }

    fetchLogs()
  }, [logContent])

  const handleOnContentChange = (e: ChangeEvent<HTMLInputElement>) => {
    setLogContent(e.currentTarget.value)
  }

  const onSubmitCreateProductLog = async () => {
    const data: ProductLogRequest = {
      content: logContent
    };
    await createProductLog(data)
    setLogContent("")
  }

  return (
    <div className="flex flex-col gap-5">
      <DialogCloud
        title={""}
        onCancel={() => { }}
        onSubmit={onSubmitCreateProductLog}>
        <div className="flex flex-col justify-center">
          <p>Insert log content</p>
          <input className="shadow-inner" onChange={handleOnContentChange} />
        </div>
      </DialogCloud>
      <div>
        <h2 className="bg-slate-300 rounded-sm p-1">Product Logs</h2>
        <ListGrid data={productLogs} Item={ProductLogItemList} />
      </div>
    </div>
  );
}
