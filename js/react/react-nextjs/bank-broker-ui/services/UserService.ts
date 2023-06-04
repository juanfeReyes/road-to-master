
import { User } from '@/model/User';
import PocketBase from 'pocketbase';

const pb = new PocketBase('http://127.0.0.1:8090');

export const getUserDetail = async (id: string): Promise<User> => {
  return (await pb.collection('users').getOne<User>(id)) as User
}
