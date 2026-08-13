'use client'

import { Button } from "@/components/common/input/Button"
import { CustomInput } from "@/components/common/input/CustomInput"
import { authClient } from "@/lib/auth-client"
import { useState } from "react"


export const SignUpForm = () => {
    const [email, setEmail] = useState('')
    const [pass, setPass] = useState('')
    const [name, setName] = useState('')

    const handleSubmit = async () => {
        const { data, error } = await authClient.signUp.email({
            email: email,
            password: pass,
            name: name
        }, {
            onSuccess: () => {
                //redirect to tasks
            }
        })
    }

    const handleOnCancel = () => {
        // redirect to landing page
    }

    return (<div className="flex flex-col gap-4">

        <div className="flex flex-col gap-2">
            <CustomInput label="name" value={name} onChange={(e) => setName(e)} />
            <CustomInput label="email" value={email} onChange={(e) => setEmail(e)} />
            <CustomInput label="password" value={pass} onChange={(e) => setPass(e)} />
        </div>

        <div className="flex gap-2">
            <Button type="Neutral" label={'Cancel'} onClick={handleOnCancel} />
            <Button type="Primary" label={'Submit'} onClick={handleSubmit} />
        </div>
    </div>)
}
