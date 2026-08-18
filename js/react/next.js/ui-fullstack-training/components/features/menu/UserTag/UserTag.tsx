'use client'

import { User } from "@/types/User"
import Image from "next/image";
import { authClient } from "@/lib/auth-client"
import { UserTagSkeleton } from "./UserTagSkeleton";

export const UserTag = () => {
    const { data: session, isPending, error } = authClient.useSession()
    
    if (isPending) return <UserTagSkeleton />

    const authUser: User = {
        name: session?.user.name,
        imageRef: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR1KpRpJFM2XX-JCMCWWPZwjL_rgzgO-jVXPg&s',
        id: session?.user.id,
        email: session?.user.email,
        isOnline: true
    };

    

    return (<div className="flex flex-row items-center gap-1 border-2 rounded-2xl border-gray-200">
        <div>
            <Image
                src={authUser.imageRef}
                width={500}
                height={500}
                alt="Authenticated user image"
                className="rounded-full aspect-square max-w-1/3 md:max-w-15 p-1"
            />
        </div>
        <div className="hidden lg:block ">
            <p>{authUser.name}</p>
            <p className="font-extralight">{authUser.isOnline ? 'Online' : 'Offline'}</p>
        </div>
    </div>)
}