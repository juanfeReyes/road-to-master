
export interface ProductLog {
  id: string,
  createTime: string,
  objectKey: string
  tags: string
}

export interface ProductLogRequest {
  content: string
}
