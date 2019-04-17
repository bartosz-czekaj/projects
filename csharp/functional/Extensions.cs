using System;
using System.Collections.Generic;
using System.Linq;

namespace functional
{
    public static class Extensions
    {
        public static Predicate<T> Negate<T>(this Predicate<T> pred) => t => !pred(t);
        

        public static Func<T, bool> Negate<T>(this Func<T, bool> pred) => t => !pred(t);

        public static List<T> QuicSort<T>(this List<T> list) where T : IComparable
        {
            if(!list.Any())
            {
                return new List<T>();
            }

            var mid = (int) Math.Floor((double)(list.Count/2));

            var pivot = list[mid];
            var small = list.Where(x=>x.CompareTo(pivot)<0).ToList();
            var same = list.Where(x=>x.CompareTo(pivot)==0).ToList();
            var large = list.Where(x=>x.CompareTo(pivot)>0).ToList();

            return small.QuicSort()
            .Concat(same)
            .Concat(large.QuicSort())
            .ToList();
        }

        public static List<T> QuicSortWithComparator<T>(this List<T> list, Comparer<T> compare)
        {
            if(!list.Any())
            {
                return new List<T>();
            }

            var mid = (int) Math.Floor((double)(list.Count/2));

            var pivot = list[mid];
            var small = list.Where(x=>compare.Compare(x, pivot)<0).ToList();
            var same = list.Where(x=>compare.Compare(x, pivot)==0).ToList();
            var large = list.Where(x=>compare.Compare(x, pivot)>0).ToList();

            return small.QuicSortWithComparator(compare)
            .Concat(same)
            .Concat(large.QuicSortWithComparator(compare))
            .ToList();
        }
    }
}