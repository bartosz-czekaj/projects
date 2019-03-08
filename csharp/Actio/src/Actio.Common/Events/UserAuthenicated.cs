namespace Actio.Common.Events
{
    public class UserAuthenicated : IEvent
    {
        public string Email {get;}

        protected UserAuthenicated()
        {
            
        }

        public UserAuthenicated(string email)
        {
            Email = email;
        }
    }
}