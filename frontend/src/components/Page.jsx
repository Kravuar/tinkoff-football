export const Page = ({children}) => {
    return (
        <div className={'bg-ambient h-full'}>
            <div className={'mx-auto max-w-page min-w-page h-full'}>
                {children}
            </div>
        </div>
    )
}