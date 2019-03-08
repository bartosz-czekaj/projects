using System;
using Actio.Common.Exceptions;
using Actio.Services.Identity.Domain.Services;

namespace Actio.Services.Identity.Domain.Models
{
    public class User
    {
        public Guid Id {get; protected set;}

        public string Email {get; protected set;}
        public string Password {get; protected set;}
        public string Salt {get; protected set;}
        public string Name {get; protected set;}
        public DateTime CretatedAt {get; protected set;}

        protected User()
        {
            
        }

        public User (string email, string name)
        {
            if (string.IsNullOrWhiteSpace(email))
            {
                throw new ActioException("empty_user_email", "User email cannot be empty.");
            }

            if (string.IsNullOrWhiteSpace(name))
            {
                throw new ActioException("empty_user_name", "User name cannot be empty.");
            }

            Email = email;
            Name = name;

            Id = Guid.NewGuid();
            CretatedAt = DateTime.UtcNow;
        }

        public void SetPassword(string password, IEncrypter encrypter)
        {
            
            if (string.IsNullOrWhiteSpace(password))
            {
                throw new ActioException("empty_password", "Password cannot be empty.");
            }
            Salt = encrypter.GetSalt();
            Password = encrypter.GetHash(password, Salt);
        }

        public bool ValidatePassword(string password, IEncrypter encrypter)
        {
            //Console.WriteLine("password: " +password);
            //return true;
            Console.WriteLine(Password);
            Console.WriteLine(password);
            Console.WriteLine(Salt);
            return Password.Equals(encrypter.GetHash(password, Salt));
        }
    }
}
