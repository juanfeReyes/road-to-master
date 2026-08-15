'use client'

import { Header } from "@/components/common/layout/Header"
import { authClient } from "@/lib/auth-client"
import { useRouter } from "next/navigation"

export const MenuFooter = () => {

    const router = useRouter()

    const handleSignOut = async () => {
        await authClient.signOut({
            fetchOptions: {
                onSuccess: () => {
                    router.push('/')
                }
            }
        })
    }

    return (<div className="flex flex-col items-end content-end pb-5 pr-3">
        <button type="button" onClick={handleSignOut}>
            <Header icon="clarity:sign-out-solid" label="Sign out" />
        </button>
    </div>)
}
