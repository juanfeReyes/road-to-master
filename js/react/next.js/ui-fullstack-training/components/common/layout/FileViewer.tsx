'use client'

import { Icon } from "@iconify/react"
import { useEffect, useState } from "react"

type FileViewerProps = {
    file: File
}

export const FileViewer = ({ file }: FileViewerProps) => {
    const [previewUrl, setPreviewUrl] = useState<string>()

    useEffect(() => {
        if (!file) {
            setPreviewUrl(undefined);
            return;
        }

        const objectUrl = URL.createObjectURL(file)
        setPreviewUrl(objectUrl);

        return () => URL.revokeObjectURL(objectUrl)
    }, [file])

    const renderContent = () => {
        if (file.type.startsWith('image/')) {
            return <img 
                src={previewUrl}
                alt="Image preview"
            />
        }

        if (file.type.startsWith('video/')) {
            return <video 
                src={previewUrl}
                controls
            />
        }

        if (file.type.startsWith('application/pdf')) {
            return <embed 
              src={previewUrl} 
              type="application/pdf" 
              width="100%" 
              height="250px" 
            />
        }

        return <p>No valid file <Icon icon='mingcute:sad-fill' /></p>
    }

    return (<div className="w-3xl h-3xl">
        { previewUrl && (
            <div className="flex flex-col gap-2">
                <h1>{file.name}</h1>
                {renderContent()}
            </div>
        ) }
    </div>)
}
