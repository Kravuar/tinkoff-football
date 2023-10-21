export const Page = ({children}) => {
    return (
        <div className={'bg-ambient min-h-[calc(100vh-64px)]'}>
            <div className={'mx-auto max-w-page'}>
                {children}
            </div>
        </div>
    )
}