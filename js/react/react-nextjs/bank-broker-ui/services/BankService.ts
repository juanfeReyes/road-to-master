import { Bank } from "@/model/Bank";
import PocketBase from 'pocketbase';


const pb = new PocketBase('http://127.0.0.1:8090');

export const getBanks = async (): Promise<Bank[]> => {
  return (await pb.collection('banks').getList<Bank>(1, 50)).items.map(bank => ({...bank}));
}

export const getBankDetails = async (id: string): Promise<Bank> => {
  return (await pb.collection('banks').getOne<Bank>(id, {expand: 'users'})) as Bank;
}
