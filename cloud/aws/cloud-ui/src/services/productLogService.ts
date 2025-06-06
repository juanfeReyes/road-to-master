import { ProductLog, ProductLogRequest } from '@/domain/ProductLog';
import { axiosInstance } from './axiosConfig';

export const getProductLogs = async (): Promise<ProductLog[]> => {
  return (await axiosInstance.get('/api/productlogs')).data
}

export const createProductLog = async (data: ProductLogRequest) => {
  await axiosInstance.post('/api/productlogs/tag', data)
}
