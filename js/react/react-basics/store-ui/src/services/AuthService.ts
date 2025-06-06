import pocketbaseEs from "pocketbase";

export const pb = new pocketbaseEs("http://localhost:8090")

export const authUser = async (username: string, password: string) => {
  return await pb.collection('users').authWithPassword(username, password);
}

export const logoutUser = () => {
  pb.authStore.clear()
}
