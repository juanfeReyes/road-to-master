import { Transaction } from "@/model/Transaction";
import PocketBase from "pocketbase";

const pb = new PocketBase("http://127.0.0.1:8090");

export const getUserDeposits = async (id: string) => {
  const deposits =  (await pb.collection("transactions").getList<Transaction>(1, 50, {
    filter: `destination~"${id}"`,
    expand: 'origin,destination',
  })).items.map((transaction) => ({...transaction}));
  return JSON.parse(JSON.stringify(deposits))
};

export const getUserWithdrawals = async (id: string): Promise<Transaction[]> => {
  const withdrawals = (await pb.collection("transactions").getList<Transaction>(1, 50, {
    filter: `origin="${id}"`,
    expand: 'origin,destination',
  })).items.map((transaction) => ({...transaction}));
  return JSON.parse(JSON.stringify(withdrawals))
};
