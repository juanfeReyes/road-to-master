import { Transaction } from "@/model/Transaction";
import PocketBase from "pocketbase";

const pb = new PocketBase("http://127.0.0.1:8090");

export const getUserDeposits = async (id: string) => {
  return (await pb.collection("transactions").getList<Transaction>(1, 50, {
    filter: `destination~"${id}"`,
  })).items.map((transaction) => ({...transaction}));
};

export const getUserWithdrawals = async (id: string): Promise<Transaction[]> => {
  return (await pb.collection("transactions").getList<Transaction>(1, 50, {
    filter: `origin="${id}"`,
  })).items.map((transaction) => ({...transaction}));
};
