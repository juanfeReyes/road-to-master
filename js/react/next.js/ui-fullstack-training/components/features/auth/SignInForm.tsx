'use client'

import { NeutralButton, PrimaryButton } from "@/components/common/input/button/Button"
import { CustomInput } from "@/components/common/input/customInput/CustomInput"
import { PasswordInput } from "@/components/common/input/PasswordInput"
import { useNotification } from "@/components/common/interactivity/useNotification"
import { Header } from "@/components/common/layout/Header"
import { authClient } from "@/lib/auth-client"
import { useRouter } from "next/navigation"
import { useState } from "react"
import * as z from 'zod'

const formSchema = z.object({
    email: z.email(),
    pass: z.string().min(9, 'Password is too short'),
})

type Form = z.infer<typeof formSchema>;

export const SignInForm = () => {
    const [form, setForm] = useState<Form>({
        pass: '',
        email: ''
    })
    const [errors, setErrors] = useState({})
    const router = useRouter()
    const { notify } = useNotification()

    const validate = () => {
        const result = formSchema.safeParse(form)
        if (result.error) {
            const errors = z.flattenError(result.error).fieldErrors
            setErrors(errors)
            return;
        }

        return result;
    }

    const handleSubmit = async () => {

         const result = validate()

        if (!result) {
            return;
        } 

        const { data, error } = await authClient.signIn.email({
            email: result.data.email,
            password: result.data.pass,
        }, {
            onSuccess: () => {
                router.push('/tasks')
            },
            onError: (ctx) => {
                console.log(ctx)
                notify({ type: 'ERROR', value: ctx.error.message })
            }
        })
    }

    const handleOnCancel = () => {
        router.push('/')
    }

    return (<div className="flex flex-col gap-4 rounded-xl max-w-xl shadow-2xl px-2 py-4 bg-white">
        <Header icon="ph:traffic-sign-thin" label="Sign In" />
        <div className="flex flex-col gap-2">
            <CustomInput label="Email" value={form.email} onChange={(e) => setForm({...form, email: e})} error={errors['email']} />
            <PasswordInput label="Password" value={form.pass} onChange={(e) => setForm({...form, pass: e})} error={errors['pass']} />
        </div>

        <div className="flex gap-4 justify-center">
            <NeutralButton label={'Cancel'} onClick={handleOnCancel} />
            <PrimaryButton label={'Sign In'} onClick={handleSubmit} />
        </div>
    </div>)
}
