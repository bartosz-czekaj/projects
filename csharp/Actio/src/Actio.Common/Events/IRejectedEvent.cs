namespace Actio.Common.Events
{
    public interface IRejectedEvent
    {
         string Reason {get;}
         string Code {get;}
    }
}