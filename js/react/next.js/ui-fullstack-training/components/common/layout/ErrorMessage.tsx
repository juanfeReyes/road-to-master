
type ErrorMessageProps = {
    error?: string | string[]
}

export const ErrorMessage = ({ error }: ErrorMessageProps) => {

    if (Array.isArray(error)) {
        return (<div className="flex flex-col">
            {error.map((msg) => <div key={msg} className="text-red-800">{msg}</div>)}
        </div>)

    }

    return (<>
        {error &&
            <div key={error} className="text-red-800">{error}</div>
        }
    </>)
}
