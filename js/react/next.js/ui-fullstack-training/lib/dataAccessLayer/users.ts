import { headers } from "next/headers";
import { cache } from "react";
import { auth } from "../auth";

export const verifySession = cache(async () => {
    const session = await auth.api.getSession({
        headers: await headers()
    })

    if (!session) {
        return null
    }


    return {userId: session.user.id}
})
