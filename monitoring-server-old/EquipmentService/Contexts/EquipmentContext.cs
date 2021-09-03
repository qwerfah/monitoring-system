using EquipmentService.Models;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;
using System;

namespace EquipmentService.Contexts
{
    public class EquipmentContext : DbContext
    {
        public DbSet<EquipmentModel> EquipmentModels { get; set; }
        public DbSet<Equipment> Equipments { get; set; }
        public DbSet<Param> Params { get; set; }

        public EquipmentContext(DbContextOptions options) : base(options)
        {
            Database.EnsureCreated();
        }

        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            if (optionsBuilder is null)
            {
                throw new ArgumentNullException(nameof(optionsBuilder));
            }

            optionsBuilder.EnableSensitiveDataLogging();
        }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            if (modelBuilder is null)
            {
                throw new ArgumentNullException(nameof(modelBuilder));
            }

            modelBuilder.ApplyConfiguration(new EquipmentModelsConfiguration());
            modelBuilder.ApplyConfiguration(new EquipmentsConfiguration());
            modelBuilder.ApplyConfiguration(new ParamsConfiguration());
        }
    }

    public class EquipmentModelsConfiguration : IEntityTypeConfiguration<EquipmentModel>
    {
        public void Configure(EntityTypeBuilder<EquipmentModel> builder)
        {
            if (builder is null)
            {
                throw new ArgumentNullException(nameof(builder));
            }

            builder.ToTable("EquipmentModels");

            builder.HasAlternateKey(em => em.EquipmentModelUid);

            builder.Property(em => em.Name).IsRequired();
        }
    }

    public class EquipmentsConfiguration : IEntityTypeConfiguration<Equipment>
    {
        public void Configure(EntityTypeBuilder<Equipment> builder)
        {
            if (builder is null)
            {
                throw new ArgumentNullException(nameof(builder));
            }

            builder.ToTable("Equipments");

            builder.HasAlternateKey(e => e.EquipmentUid);

            builder.Property(e => e.Status).IsRequired();

            builder
                .HasOne(e => e.EquipmentModel)
                .WithMany(em => em.Equipments)
                .HasForeignKey(e => e.EquipmentModelId)
                .OnDelete(DeleteBehavior.Cascade);
        }
    }

    public class ParamsConfiguration : IEntityTypeConfiguration<Param>
    {
        public void Configure(EntityTypeBuilder<Param> builder)
        {
            if (builder is null)
            {
                throw new ArgumentNullException(nameof(builder));
            }

            builder.ToTable("Params");

            builder.HasAlternateKey(p => p.ParamUid);

            builder.Property(p => p.Name).IsRequired();
            builder.Property(p => p.MeasurementUnits).IsRequired();

            builder
                .HasOne(e => e.EquipmentModel)
                .WithMany(em => em.Params)
                .HasForeignKey(e => e.EquipmentModelId)
                .OnDelete(DeleteBehavior.Cascade);
        }
    }
}
